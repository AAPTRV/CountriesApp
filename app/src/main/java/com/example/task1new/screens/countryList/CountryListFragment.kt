package com.example.task1new.screens.countryList

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.data.utils.*
import com.example.task1new.*
import com.example.task1new.base.mvvm.BaseMvvmView
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.databinding.FragmentCountryListBinding
import com.example.data.ext.showSimpleDialogNetworkError
import com.example.task1new.util.StartSnapHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel

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
private const val MENU_FILTER_ICON_STATE = "menu filter icon state"

class CountryListFragment : ScopeFragment(), BaseMvvmView {

    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentCountryListBinding? = null
    private var sortIconClipped = false
    private var filterIconClipped = false
    private lateinit var mLayoutManagerState: Parcelable
    private var mLocationProviderClient: FusedLocationProviderClient? = null
    private val mViewModel: CountryListViewModel by stateViewModel()

    private lateinit var mFilterButton: MenuItem
    private lateinit var mMapsButton: MenuItem
    private lateinit var mSortButton: MenuItem

    init {
        Log.e("HZ", "Initialization FRAGMENT BLOCK")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("HZ", "ON CREATE")
        //GPS
        mLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())
        getCurrentLocation()
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
        //RESULT LISTENER
        setFragmentResultListener(FRAGMENT_FOR_RESULT_FILTER_KEY) { _, bundle ->
            val result =
                bundle.getParcelableArrayList<Parcelable>(FRAGMENT_FOR_RESULT_RESULT_LIST) as List<Double>
            mViewModel.setFilterMaxArea(result[0])
            mViewModel.setFilterMinArea(result[1])
            mViewModel.setFilterMaxPopulation(result[2].toInt())
            mViewModel.setFilterMinPopulation(result[3].toInt())
            mViewModel.setFilterMaxDistance(result[4])
        }

        //General UI settings
        setHasOptionsMenu(true)

        // TODO: Handle saved instance state ...
        binding?.recycleView?.layoutManager = LinearLayoutManager(context)
        val snapHelper: SnapHelper = StartSnapHelper() // или PagerSnapHelper()
        binding?.recycleView?.let { snapHelper.attachToRecyclerView(binding?.recycleView) }


        //MVVM OBSERVE
        mViewModel.getCountriesListLiveData().observe(viewLifecycleOwner, { outcome ->
            when (outcome) {
                is Outcome.Progress -> {
                    if (outcome.loading) {
                        showProgress()
                    } else {
                        hideProgress()
                    }
                }
                is Outcome.Next -> {
                    myAdapter.submitList(outcome.data)
                    hideProgress()
                    myAdapter.repopulateAdapterData(outcome.data)
                }
                is Outcome.Failure -> {
                    showError(outcome.e.toString(), outcome.e)
                }
                is Outcome.Success -> {
                    myAdapter.submitList(outcome.data)
                    hideProgress()
                    myAdapter.repopulateAdapterData(outcome.data)
                }
            }
        })
        mViewModel.getFilterLiveData().observe(viewLifecycleOwner, {
            myAdapter.submitList(mViewModel.getFilteredDataFromCountriesLiveData())
            Toast.makeText(this.requireActivity(), "Filter updated", Toast.LENGTH_SHORT).show()
        })

        //RecycleView
        binding?.recycleView?.setHasFixedSize(true)
        binding?.recycleView?.adapter = myAdapter
        myAdapter.setItemClick {
            val bundle = Bundle()
            bundle.putString(COUNTRY_NAME_BUNDLE_KEY, it.name)
            findNavController().navigate(
                R.id.action_blankFragmentRV_to_countryDetailsFragment, bundle
            )
        }
    }

    override fun onPause() {
        super.onPause()
        mLayoutManagerState = binding?.recycleView?.layoutManager?.onSaveInstanceState()!!
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.actionBar?.title
        activity?.actionBar?.subtitle
        inflater.inflate(R.menu.countries_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)




        mFilterButton = menu.findItem(R.id.menu_filter_button)
        mMapsButton = menu.findItem(R.id.menu_maps_button)
        mSortButton = menu.findItem(R.id.menu_sort_button)

        loadMenuSortIconState()
        initializeMenuSortIconSet(menu.findItem(R.id.menu_sort_button))
        initializeFilterIconSet(menu.findItem(R.id.menu_filter_button))

        //Initialize menu search button
        val menuSearchItem = menu.findItem(R.id.menu_search_button)
        val mSvMenu: SearchView = menuSearchItem.actionView as SearchView

        mSvMenu.setOnSearchClickListener {
            mFilterButton.isVisible = false
            mMapsButton.isVisible = false
            mSortButton.isVisible = false
        }

        mSvMenu.setOnCloseListener {
            mFilterButton.isVisible = true
            mMapsButton.isVisible = true
            mSortButton.isVisible = true
            false }

        mSvMenu.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    mViewModel.setFilterCountryName(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length >= 3) {
                    mViewModel.setFilterCountryName(newText)
                }
                if (newText.length in 0..2) {
                    mViewModel.setFilterCountryName(com.example.data.utils.FILTER_ANY_COUNTRY_VALUE)
                }
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO: Make filter item GONE until we have some values in LiveData(in order to avoid crash while clicking
        // TODO: Handle filter range slider while it is impossible to detect users location
        if (item.itemId == R.id.menu_maps_button) {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_blankFragmentRV_to_mapsFragmentBlank2)
        }
        if (item.itemId == R.id.menu_filter_button) {
            if (!filterIconClipped) {
                val bundle = Bundle()
                bundle.putString(
                    com.example.data.utils.ADAPTER_MAXIMUM_POPULATION_BUNDLE_KEY,
                    mViewModel.getMaximumPopulation()
                )
                bundle.putString(
                    com.example.data.utils.ADAPTER_MINIMUM_POPULATION_BUNDLE_KEY,
                    mViewModel.getMinimumPopulation()
                )
                bundle.putString(
                    com.example.data.utils.ADAPTER_MAXIMUM_AREA_BUNDLE_KEY,
                    mViewModel.getMaximumArea()
                )
                bundle.putString(
                    com.example.data.utils.ADAPTER_MINIMUM_AREA_BUNDLE_KEY,
                    mViewModel.getMinimumArea()
                )
                bundle.putString(
                    com.example.data.utils.ADAPTER_MAXIMUM_DISTANCE_BUNDLE_KEY,
                    mViewModel.getMaximumDistance()
                )
                filterIconClipped = true
                saveMenuFilterIconState()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_blankFragmentRV_to_filterFragment, bundle)
            } else {
                item.setIcon(R.drawable.ic_baseline_filter_alt_24)
                // TODO: Check clear filter for default value -> if gps is off it
                mViewModel.clearFilterExceptName()
                filterIconClipped = false
            }
        }
        if (item.itemId == R.id.menu_sort_button) {
            // TODO: Sort items automatically at start (there is a problem while back from the details with layout manager (scroll))
            updateMenuSortIconView(item)
            saveMenuSortIconState()
            if (sortIconClipped) {
                // TODO: 05.09.2021 SORT IN A GOOD WAY!
                myAdapter.sortAscendingDataListInAdapter()
            } else {
                myAdapter.sortDescendingDataListInAdapter()
            }
        }
//        if (item.itemId == R.id.menu_search_button) {
//            mFilterButton.isVisible = false
//            Log.e("HZ", "MENU SEARCH BUTTON TAPPED")
//        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveMenuFilterIconState() {
        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences(
            SHARED_PREFS, AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(MENU_FILTER_ICON_STATE, filterIconClipped)
        editor.apply()
        Toast.makeText(this.requireActivity(), "Data Saved", Toast.LENGTH_SHORT).show()
    }

    private fun initializeFilterIconSet(item: MenuItem) {
        if (filterIconClipped) {
            item.setIcon(R.drawable.ic_baseline_filter_off_alt_24)
        } else {
            item.setIcon(R.drawable.ic_baseline_filter_alt_24)
        }
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

    private fun initializeMenuSortIconSet(item: MenuItem) {
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            COUNTRY_DETAILS_LAYOUT_MANAGER_KEY,
            mLayoutManagerState
        )
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val task: Task<Location>? = mLocationProviderClient?.lastLocation
            task?.addOnSuccessListener { location ->
                location?.let { mViewModel.attachCurrentLocation(location) }
                if (location == null) {
                    mViewModel.attachCurrentLocation(Location(LocationManager.GPS_PROVIDER).apply {
                        latitude = 53.06
                        longitude = 126.19
                    })
                }
                mViewModel.getCountriesFromDbRx()
                mViewModel.getCountriesFromAPI()
            }
        } else {
            val listPermissionsNeeded = ArrayList<String>()
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                listPermissionsNeeded.toTypedArray(),
                GPS_PERMISSION
            )
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

        //var myAdapter: CountryListAdapter = CountryListAdapter()
        var myAdapter: MvvmListAdapter = MvvmListAdapter()

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountryListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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