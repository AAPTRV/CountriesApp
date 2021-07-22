package com.example.task1new.screens.countryList

import android.content.ContentValues
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.task1new.COUNTRY_DETAILS_LAYOUT_MANAGER_KEY
import com.example.task1new.COUNTRY_NAME_BUNDLE_KEY
import com.example.task1new.OkRetrofit
import com.example.task1new.R
import com.example.task1new.databinding.FragmentBlankRVBinding
import com.example.task1new.databinding.FragmentCountryDetailsBinding
import com.example.task1new.dto.PostCountryItemDto
import com.example.task1new.ext.convertCommonInfoAPIDatatoDBItem
import com.example.task1new.ext.convertLanguagesAPIDataToDBItem
import com.example.task1new.model.PostCountryItemModel
import com.example.task1new.model.convertToPostCountryItemDto
import com.example.task1new.room.*
import com.example.task1new.transformer.DaoEntityToDtoTransformer
import com.trendyol.bubblescrollbarlib.BubbleTextProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_blank_r_v.*
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

    private var mDatabase: DBInfo? = null

    private var binding: FragmentBlankRVBinding? = null

    var sortIconClipped = false

    private lateinit var mLayoutManagerState: Parcelable

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
        binding = FragmentBlankRVBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        mDatabase = context?.let { DBInfo.init(it) }

        val daoCountryInfo = mDatabase?.getCountryCommonInfoDAO()
        val daoLanguageInfo = mDatabase?.getLanguageCommonInfoDAO()

        getInitialDataFromDB(daoCountryInfo, daoLanguageInfo)
        getData(daoCountryInfo, daoLanguageInfo)

        binding?.recycleView?.setHasFixedSize(true)
        myAdapter.setItemClick {
            val bundle = Bundle()
            bundle.putString(COUNTRY_NAME_BUNDLE_KEY, it.name)
            findNavController().navigate(
                R.id.action_blankFragmentRV_to_countryDetailsFragment,
                bundle
            )
        }

        binding?.recycleView?.adapter = myAdapter
        binding?.bubbleScroll?.attachToRecyclerView(binding?.recycleView!!)
        binding?.bubbleScroll?.bubbleTextProvider = BubbleTextProvider {
//            myAdapter.getCurrentAdapterPosition().toString()
            myAdapter.getDataListFromAdapter()[it].name
        }

        if (savedInstanceState != null) {
            mLayoutManagerState =
                savedInstanceState.getParcelable(COUNTRY_DETAILS_LAYOUT_MANAGER_KEY)!!
            binding?.recycleView?.layoutManager?.onRestoreInstanceState(mLayoutManagerState)
        } else {
            binding?.recycleView?.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onPause() {
        super.onPause()
        mLayoutManagerState = recycleView.layoutManager?.onSaveInstanceState()!!
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

        //Initialize menu search button
        var menuSearchItem = menu.findItem(R.id.menu_search_button)
        var mSvMenu: SearchView = menuSearchItem.actionView as SearchView
        mSvMenu.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { myAdapter.filterByName(query) }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                myAdapter.filterByName(newText)
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_sort_button) {
            updateMenuSortIconView(item)
            saveMenuSortIconState()
            if (sortIconClipped) {
                myAdapter.sortAscendingDataListInAdapter()
            } else {
                myAdapter.sortDescendingDataListInAdapter()
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
        val mPostCountriesData = mutableListOf<PostCountryItemDto>()

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
                DaoEntityToDtoTransformer.daoEntityToDtoTransformer(
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
        myAdapter = RecyclerAdapter()
        myAdapter.addListOfItems(mPostCountriesData)
        if (sortIconClipped) {
            myAdapter.sortAscendingDataListInAdapter()
        } else {
            myAdapter.sortDescendingDataListInAdapter()
        }
    }

    private fun getData(countryDao: CountryCommonInfoDAO?, languageDao: CountryLanguageDAO?) {
        OkRetrofit.jsonPlaceHolderApi.getPosts()
            .observeOn(AndroidSchedulers.mainThread()) // Where subscribe() will run
            .subscribeOn(Schedulers.io()) // Where getPosts() will run
            .subscribe({response ->

                myAdapter.addNewUniqueItems(response.convertToPostCountryItemDto())

                // DB inserting data
                val mCountriesInfoFromAPI = response.toMutableList()
                val mCountriesInfoToDB = mutableListOf<CountryDatabaseCommonInfoEntity>()

                val mLanguagesFromApiToDB = mutableListOf<CountryDatabaseLanguageInfoEntity>()
                mCountriesInfoFromAPI.slice(1..20).forEach { item ->
                    mLanguagesFromApiToDB.add(item.convertLanguagesAPIDataToDBItem())
                }
                languageDao?.deleteAll(mLanguagesFromApiToDB) // for testing purposes
                languageDao?.addAll(mLanguagesFromApiToDB)

                mCountriesInfoFromAPI.slice(1..20).forEach { item ->
                    mCountriesInfoToDB.add(
                        item.convertCommonInfoAPIDatatoDBItem()
                    )
                    countryDao?.deleteAll(mCountriesInfoToDB) // for testing purposes
                    countryDao?.addAll(mCountriesInfoToDB)
                }
            }, {throwable -> throwable.printStackTrace()})
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

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            COUNTRY_DETAILS_LAYOUT_MANAGER_KEY,
            mLayoutManagerState
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