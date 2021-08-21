package com.example.task1new.screens.map

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.task1new.R
import com.example.task1new.databinding.FragmentMapsBlankBinding
import com.example.domain.dto.LatLngDto
import com.example.data.ext.showSimpleDialogNetworkError
import com.example.task1new.base.mvvm.BaseMvvmView
import com.example.task1new.base.mvvm.Outcome
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel

// TODO: Make this fragment
class MapsFragmentBlank : ScopeFragment(), BaseMvvmView, OnMapReadyCallback {

    private var mGoogleMap: GoogleMap? = null
    private var binding: FragmentMapsBlankBinding? = null
    private var mLocationProviderClient: FusedLocationProviderClient? = null
    private val mViewModel: MapsFragmentViewModel by stateViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBlankBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())
        mapFragment?.getMapAsync(this)
        mViewModel.getLatLngDtoFromApi()

        mViewModel.getCountriesLatLngDto().observe(viewLifecycleOwner, {
            outcome ->
            when(outcome){
                is Outcome.Progress ->{
                    showProgress()
                }
                is Outcome.Next -> {
                }
                is Outcome.Failure -> {
                    hideProgress()
                }
                is Outcome.Success -> {
                    showAllCountryMarkersOnMap(outcome.data)
                    hideProgress()
                }
            }
        })
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            val task: Task<Location>? = mLocationProviderClient?.lastLocation
            task?.addOnSuccessListener {
                if (it != null) {
                    mGoogleMap?.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                it.latitude,
                                it.longitude
                            )
                        ).title("My position")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    )
                    mGoogleMap?.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                it.latitude,
                                it.longitude
                            )
                        )
                    )
                }
            }
        } else {
            val listPermissionsNeeded = ArrayList<String>()
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this.requireActivity(), listPermissionsNeeded.toTypedArray(), 44)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
    }

    private fun showAllCountryMarkersOnMap(dtoList: List<LatLngDto>) {
        for (dto in dtoList) {
            mGoogleMap?.addMarker(
                MarkerOptions().position(
                    LatLng(
                        dto.mLatitude,
                        dto.mLongitude
                    )
                ).title(dto.name)
            )
            mGoogleMap?.moveCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        dto.mLatitude,
                        dto.mLongitude
                    )
                )
            )
        }
        getCurrentLocation()
    }

    override fun showError(error: String, throwable: Throwable) {
        activity?.showSimpleDialogNetworkError()
    }

    override fun showProgress() {
        binding?.progressMaps?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressMaps?.visibility = View.GONE
    }
}