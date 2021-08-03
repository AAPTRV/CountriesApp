package com.example.task1new.screens.countryList

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task1new.*
import com.example.task1new.app.CountriesApp
import com.example.task1new.base.mvp.BaseMvpFragment
import com.example.task1new.databinding.FragmentCountryListBinding
import com.example.task1new.dto.CountryDto
import com.example.task1new.ext.showSimpleDialogNetworkError
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.trendyol.bubblescrollbarlib.BubbleTextProvider

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CountryListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val SHARED_PREFS: String = "sharedPrefs"
private const val MENU_SORT_ICON_STATE = "menu sort icon state"


class CountryListFragment : BaseMvpFragment<CountryListView, CountryListPresenter>(),
    CountryListView {

    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentCountryListBinding? = null

    private var sortIconClipped = false

    private lateinit var mLayoutManagerState: Parcelable

    private var mLocationProviderClient: FusedLocationProviderClient? = null

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
        binding = FragmentCountryListBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        getCurrentLocation()

        getPresenter().attachView(this)
        getPresenter().getDataFromDBToRecycleAdapter()
        getPresenter().getDataFromRetrofitToRecycleAdapter(false)

        binding?.recycleView?.setHasFixedSize(true)
        myAdapter.setItemClick {
            myAdapter.notifyDataSetChanged()
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
        mLayoutManagerState = binding?.recycleView?.layoutManager?.onSaveInstanceState()!!
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.countries_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        loadMenuSortIconState()
        initialMenuSortIconSet(menu.findItem(R.id.menu_sort_button))

        //Initialize menu search button
        val menuSearchItem = menu.findItem(R.id.menu_search_button)
        val mSvMenu: SearchView = menuSearchItem.actionView as SearchView

        mSvMenu.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    getPresenter().getSearchSubject().onNext(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                getPresenter().getSearchSubject().onNext(newText)
                if (newText.length == 2 && myAdapter.isFiltered()) {
                    myAdapter.restoreFilteredListFromDataList()
                }
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_maps_button) {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_blankFragmentRV_to_mapsFragmentBlank2)
        }
        if(item.itemId == R.id.menu_filter_button){
            val bundle = Bundle()
            bundle.putString(ADAPTER_MAXIMUM_AREA_BUNDLE_KEY, myAdapter.getMaximumArea())
            bundle.putString(ADAPTER_MAXIMUM_DISTANCE_BUNDLE_KEY, myAdapter.getMaximumDistance())
            bundle.putString(ADAPTER_MAXIMUM_POPULATION_BUNDLE_KEY, myAdapter.getMaximumPopulation())
            bundle.putString(ADAPTER_MINIMUM_AREA_BUNDLE_KEY, myAdapter.getMinimumArea())
            bundle.putString(ADAPTER_MINIMUM_DISTANCE_BUNDLE_KEY, myAdapter.getMinimumDistance())
            bundle.putString(ADAPTER_MINIMUM_POPULATION_BUNDLE_KEY, myAdapter.getMinimumPopulation())
            Navigation.findNavController(requireView())
                .navigate(R.id.action_blankFragmentRV_to_filterFragment, bundle)
        }
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

    override fun repopulateFilteredDataListInAdapter(data: List<CountryDto>){
        myAdapter.repopulateFilteredDataList(data)
    }

    override fun addNewUniqueItemsInRecycleAdapter(data: List<CountryDto>) {
        myAdapter.addNewUniqueItems(data)
    }

    private fun loadMenuSortIconState() {
        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences(
            SHARED_PREFS,
            AppCompatActivity.MODE_PRIVATE
        )
        sortIconClipped = sharedPreferences.getBoolean(MENU_SORT_ICON_STATE, false)
    }

    private fun saveMenuSortIconState() {
        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences(
            SHARED_PREFS, AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(MENU_SORT_ICON_STATE, sortIconClipped)
        editor.apply()
        Toast.makeText(this.requireActivity(), "Data Saved", Toast.LENGTH_SHORT).show()
    }

    private fun initialMenuSortIconSet(item: MenuItem) {
        if (sortIconClipped) {
            item.setIcon(R.drawable.ic_ascending_sort)
        } else {
            item.setIcon(R.drawable.ic_descending_sort)
        }
    }

    private fun updateMenuSortIconView(item: MenuItem) {
        sortIconClipped = if (!sortIconClipped) {
            item.setIcon(R.drawable.ic_ascending_sort)
            true
        } else {
            item.setIcon(R.drawable.ic_descending_sort)
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        getPresenter().onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            COUNTRY_DETAILS_LAYOUT_MANAGER_KEY,
            mLayoutManagerState
        )
    }

    private fun getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            val task: Task<Location>? = mLocationProviderClient?.lastLocation
            task?.addOnSuccessListener { location ->
                location?.let { myAdapter.attachCurrentLocation(location)}
            }
        } else {
            val listPermissionsNeeded = ArrayList<String>()
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this.requireActivity(), listPermissionsNeeded.toTypedArray(), 44)
        }
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

        var myAdapter: CountryListAdapter = CountryListAdapter()

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountryListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun createPresenter() {
        mPresenter = CountryListPresenter(CountriesApp.mDatabase)
    }

    override fun getPresenter(): CountryListPresenter {
        return mPresenter
    }

    override fun showError(error: String, throwable: Throwable) {
        activity?.showSimpleDialogNetworkError()
    }

    override fun showProgress() {
        binding?.progressList?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressList?.visibility = View.GONE
    }
}