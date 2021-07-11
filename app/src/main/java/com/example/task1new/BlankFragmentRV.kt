package com.example.task1new

import android.content.ContentValues
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1new.ext.addNewItemsToResponseBody
import com.example.task1new.ext.convertLanguagesAPIDataToDBItem
import com.example.task1new.ext.convertToCountryNameList

import com.example.task1new.model.PostCountryItem
import com.example.task1new.room.*
import com.example.task1new.transformer.DaoEntityToAPITransformer
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragmentRV.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val SHARED_PREFS: String = "sharedPrefs"
private const val MENU_SORT_ICON_STATE = "menu sort icon state"


class BlankFragmentRV : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var myAdapter: RecyclerAdapter
    lateinit var responseBody: MutableList<PostCountryItem>
    lateinit var recycleView: RecyclerView

    private var mDatabase: DBInfo? = null

    var sortIconClipped = false

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
        setHasOptionsMenu(true)
        recycleView = view.findViewById(R.id.recycleView)
        recycleView.setHasFixedSize(true)
        recycleView.layoutManager = LinearLayoutManager(context)

        mDatabase = context?.let { DBInfo.init(it) }

        val daoCountryInfo = mDatabase?.getCountryCommonInfoDAO()
        val daoLanguageInfo = mDatabase?.getLanguageCommonInfoDAO()

        getInitialDataFromDB(daoCountryInfo, daoLanguageInfo)
        getData(daoCountryInfo, daoLanguageInfo)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.countries_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(
            ContentValues.TAG,
            "(LOG_ICON) ON_CREATE_OPTIONS_LOAD_MENU sortIconClipped = $sortIconClipped"
        )
        loadMenuSortIconState()
        initialMenuSortIconSet(menu.findItem(R.id.menu_sort_button))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.itemSortUp) {
            sortDescending()
        }
        if (item.itemId == R.id.itemSortDown) {
            sortAscending()
        }
        if (item.itemId == R.id.menu_sort_button) {
            updateMenuSortIconView(item)
            saveMenuSortIconState()
            if (sortIconClipped) {
                sortAscending()
            } else {
                sortDescending()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getInitialDataFromDB(
        countryDao: CountryCommonInfoDAO?,
        languageDao: CountryLanguageDAO?
    ) {

        //BD reading data (initializing variables for entities)
        val mCountriesLanguageEntities = mutableListOf<CountryDatabaseLanguageInfoEntity>()
        val mCountriesInfoEntities = mutableListOf<CountryDatabaseCommonInfoEntity>()
        val mPostCountriesData = mutableListOf<PostCountryItem>()

        // Filling mCountriesInfoEntities list with items from DB
        countryDao?.getAllInfo()?.forEach { entity ->
            mCountriesInfoEntities.add(entity)
        }

        // Filling mCountriesLanguageEntities with items from DB
        mCountriesInfoEntities.forEach { entity ->
            mCountriesLanguageEntities.add(languageDao?.getLanguageInfoByCountry(entity.name)!!)
        }

        // Filling mPost Countries Data through transformer, using info and languages entities
        mCountriesInfoEntities.forEachIndexed { index, infoEntity ->
            mPostCountriesData.add(
                DaoEntityToAPITransformer.daoEntityToApiTransformer(
                    infoEntity,
                    mCountriesLanguageEntities[index]
                )
            )
        }

        Log.d(
            ContentValues.TAG,
            "DB TEST mPostCountryData.size = ${mPostCountriesData.size}"
        )

        // Filling adapter with first 20 items from DB
        responseBody = mPostCountriesData
        myAdapter = RecyclerAdapter(responseBody)
        recycleView.adapter = myAdapter
        if (sortIconClipped) {
            sortAscending()
        } else {
            sortDescending()
        }
    }

    private fun getData(countryDao: CountryCommonInfoDAO?, languageDao: CountryLanguageDAO?) {

        val retrofitData = OkRetrofit.retrofitData


        retrofitData.enqueue(object : retrofit2.Callback<List<PostCountryItem>?> {
            override fun onFailure(call: Call<List<PostCountryItem>?>, t: Throwable) {
                Log.d(ContentValues.TAG, "On Failure: " + t.message)
            }

            override fun onResponse(
                call: Call<List<PostCountryItem>?>,
                response: Response<List<PostCountryItem>?>
            ) {

                responseBody.addNewItemsToResponseBody(response.body()!!.toMutableList())
                responseBody.removeAll { it.capital == "" }
                myAdapter = RecyclerAdapter(responseBody)
                recycleView.adapter = myAdapter
                if (sortIconClipped) {
                    sortAscending()
                } else {
                    sortDescending()
                }

                // DB inserting data
                val mCountriesInfoFromAPI = response.body()!!.toMutableList()
                val mCountriesInfoToDB = mutableListOf<CountryDatabaseCommonInfoEntity>()

                val mLanguagesFromApiToDB = mutableListOf<CountryDatabaseLanguageInfoEntity>()
                mCountriesInfoFromAPI.slice(1..20).forEach { item ->
                    mLanguagesFromApiToDB.add(item.convertLanguagesAPIDataToDBItem())
                }
                languageDao?.deleteAll(mLanguagesFromApiToDB)
                languageDao?.addAll(mLanguagesFromApiToDB)


                mCountriesInfoFromAPI.slice(1..20).forEach { item ->
                    mCountriesInfoToDB.add(
                        CountryDatabaseCommonInfoEntity(
                            item.name,
                            item.capital,
                            item.population,
                            item.languages.convertToCountryNameList()
                        )
                    )
                    countryDao?.deleteAll(mCountriesInfoToDB)
                    countryDao?.addAll(mCountriesInfoToDB)
                }
            }
        })
    }

    private fun loadMenuSortIconState() {
        Log.d(
            ContentValues.TAG,
            "(LOG_ICON) LOAD_MENU_SORT_ICON_STATE (BEFORE) sortIconClipped = $sortIconClipped"
        )
        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences(
            SHARED_PREFS,
            AppCompatActivity.MODE_PRIVATE
        )
        sortIconClipped = sharedPreferences.getBoolean(MENU_SORT_ICON_STATE, false)
        Log.d(
            ContentValues.TAG,
            "(LOG_ICON) LOAD_MENU_SORT_ICON_STATE (AFTER) sortIconClipped = $sortIconClipped"
        )
    }

    private fun sortAscending() {
        responseBody.sortBy { it.population }
        myAdapter.notifyDataSetChanged()
    }

    private fun sortDescending() {
        responseBody.sortByDescending { it.population }
        myAdapter.notifyDataSetChanged()
    }

    private fun saveMenuSortIconState() {
        Log.d(
            ContentValues.TAG,
            "(LOG_ICON) SAVE_MENU_ICON_STATE (BEFORE) sortIconClipped = $sortIconClipped"
        )
        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences(
            SHARED_PREFS, AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(MENU_SORT_ICON_STATE, sortIconClipped)
        editor.apply()
        Toast.makeText(this.requireActivity(), "Data Saved", Toast.LENGTH_SHORT).show()
        Log.d(
            ContentValues.TAG,
            "(LOG_ICON) SAVE_MENU_ICON_STATE (AFTER) sortIconClipped = $sortIconClipped"
        )
    }

    private fun initialMenuSortIconSet(item: MenuItem) {
        Log.d(
            ContentValues.TAG,
            "(LOG_ICON) INITIAL_MENU_SORT_ICON_SET (BEFORE) sortIconClipped = $sortIconClipped"
        )
        if (sortIconClipped) {
            item.setIcon(R.drawable.ic_ascending_sort)
        } else {
            item.setIcon(R.drawable.ic_descending_sort)
        }
        Log.d(
            ContentValues.TAG,
            "(LOG_ICON) INITIAL_MENU_SORT_ICON_SET (AFTER) sortIconClipped = $sortIconClipped"
        )
    }

    private fun updateMenuSortIconView(item: MenuItem) {
        Log.d(
            ContentValues.TAG,
            "(LOG_ICON) UPDATE_MENU_ICON_STATE (BEFORE) sortIconClipped = $sortIconClipped"
        )
        if (!sortIconClipped) {
            item.setIcon(R.drawable.ic_ascending_sort)
            sortIconClipped = true
        } else {
            item.setIcon(R.drawable.ic_descending_sort)
            sortIconClipped = false
        }
        Log.d(
            ContentValues.TAG,
            "(LOG_ICON) UPDATE_MENU_ICON_STATE (AFTER) sortIconClipped = $sortIconClipped"
        )
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