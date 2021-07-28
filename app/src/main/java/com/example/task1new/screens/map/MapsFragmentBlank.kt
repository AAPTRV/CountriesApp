package com.example.task1new.screens.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.task1new.R
import com.example.task1new.base.mvp.BaseMvpFragment
import com.example.task1new.databinding.FragmentMapsBlankBinding
import com.example.task1new.dto.LatLngDto
import com.example.task1new.ext.showSimpleDialogNetworkError
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragmentBlank : BaseMvpFragment<MapsView, MapsPresenter>(), OnMapReadyCallback, MapsView {

    private var mGoogleMap: GoogleMap? = null
    private var binding: FragmentMapsBlankBinding? = null

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
        mapFragment?.getMapAsync(this)
        getPresenter().attachView(this)
        getPresenter().getDataFromRetrofitToShowMarkers()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
    }

    override fun showAllCountryMarkersOnMap(dtoList: List<LatLngDto>) {
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

    override fun createPresenter() {
        mPresenter = MapsPresenter()
    }

    override fun getPresenter(): MapsPresenter {
        return mPresenter
    }
}