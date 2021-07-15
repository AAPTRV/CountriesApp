package com.example.task1new.screens.details

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.task1new.COUNTRY_NAME_BUNDLE_KEY
import com.example.task1new.OkRetrofit
import com.example.task1new.R
import com.example.task1new.ext.loadSvg
import com.example.task1new.ext.showDialogWithOneButton
import com.example.task1new.model.PostCountryItemModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryDetailsFragment : Fragment() {

    private lateinit var progress: FrameLayout
    private lateinit var mSrCountryDetails: SwipeRefreshLayout
    private lateinit var mLanguagesAdapter: LanguageAdapter
    private lateinit var mRvLanguages: RecyclerView
    private lateinit var mTvCountryName: AppCompatTextView
    private lateinit var mCountryName: String
    private lateinit var mIvCountryFlag: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mCountryName = arguments?.getString(COUNTRY_NAME_BUNDLE_KEY, "") ?: ""

        return inflater.inflate(R.layout.fragment_country_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRvLanguages = view.findViewById(R.id.rv_country_details_languages)
        mTvCountryName = view.findViewById(R.id.country_name)
        mSrCountryDetails = view.findViewById(R.id.sr_country_details)
        progress = view.findViewById(R.id.progress)
        mIvCountryFlag = view.findViewById(R.id.iv_country_flag)

        mTvCountryName.text = mCountryName

        mLanguagesAdapter = LanguageAdapter()
        mRvLanguages.adapter = mLanguagesAdapter

        mSrCountryDetails.setOnRefreshListener {
            getCountryByName(true)
        }
        getCountryByName(false)
        activity?.showDialogWithOneButton("Title", "Description", R.string.ok, null)
    }

    private fun getCountryByName(isRefresh: Boolean) {
        progress.visibility = if (isRefresh) View.GONE else View.VISIBLE
        OkRetrofit.jsonPlaceHolderApi.getCountryByName(mCountryName)
            .enqueue(object : Callback<List<PostCountryItemModel>> {
                override fun onResponse(
                    call: Call<List<PostCountryItemModel>>,
                    response: Response<List<PostCountryItemModel>>
                ) {
                    Log.e("hz", response.body().toString())
                    mLanguagesAdapter.addListOfItems(
                        response.body()?.get(0)?.convertToPostCountryItemDto()?.languages
                            ?: mutableListOf()
                    )
                    mSrCountryDetails.isRefreshing = false
                    mIvCountryFlag.loadSvg(response.body()?.get(0)?.flagImageUrl.toString())
                    Thread.sleep(1000)
                    progress.visibility = View.GONE
                }

                override fun onFailure(call: Call<List<PostCountryItemModel>>, t: Throwable) {
                    t.printStackTrace()
                    mSrCountryDetails.isRefreshing = false
                    progress.visibility = View.GONE
                }

            })
    }

}