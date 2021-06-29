package com.example.task1new

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_blank_r_v.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Time
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragmentRV.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragmentRV : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var myAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_r_v, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleView.setHasFixedSize(true)
        recycleView.layoutManager = LinearLayoutManager(context)
        getData(switch1.isChecked)

        switch1.setOnCheckedChangeListener{ _, _ ->
            getData(switch1.isChecked)
        }

    }
    private fun getData(setOn: Boolean){

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(NetConstants.SESSION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(NetConstants.SESSION_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .build()

        val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetConstants.SERVER_API_BASE_URL)
                .client(okHttpClient)
                .build()

        val jsonPlaceHolderApi = retrofitBuilder.create(JsonPlaceHolderApi::class.java)
        val retrofitData = jsonPlaceHolderApi.getPosts()


        retrofitData.enqueue(object : retrofit2.Callback<List<Post>?> {
            override fun onFailure(call: Call<List<Post>?>, t: Throwable) {
                Log.d(ContentValues.TAG, "On Failure: " + t.message)
            }

            override fun onResponse(call: Call<List<Post>?>, response: Response<List<Post>?>) {
                val responseBody: MutableList<Post> = response.body()!!.toMutableList()
                responseBody.removeAll {it.capital == ""}
                if (setOn){
                    responseBody.sortBy{it.population}
                } else {  responseBody.sortByDescending {it.population} }
                myAdapter = RecyclerAdapter(responseBody)
                myAdapter.notifyDataSetChanged()
                recycleView.adapter = myAdapter
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragmentRV.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragmentRV().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}