package com.example.task1new.screens.map

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.task1new.R
import com.example.task1new.base.mvp.BaseMvpFragment
import com.example.task1new.databinding.FragmentMapsBlankBinding
import com.example.task1new.ext.showSimpleDialogNetworkError
import com.example.task1new.model.PostCountryItemModel
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
        Log.e(ContentValues.TAG, "beforePresenter")
        getPresenter().getDataFromRetrofitToShowMarkers()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val sydney = LatLng(-34.0, 151.0)
        mGoogleMap = googleMap
        mGoogleMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun showAllCountryMarkersOnMap(countriesModels: List<PostCountryItemModel>) {
        for (item in countriesModels) {
            val location = item.convertToLatLngDto()
            val country = item.convertToPostCountryItemDto()
            mGoogleMap?.addMarker(
                MarkerOptions().position(
                    LatLng(
                        location.mLatitude,
                        location.mLongitude
                    )
                ).title("Marker in ${country.name}")
            )
            mGoogleMap?.moveCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        location.mLatitude,
                        location.mLongitude
                    )
                )
            )
        }
    }

    override fun showError(error: String, throwable: Throwable) {
        activity?.showSimpleDialogNetworkError()
    }

    override fun showProgress() {
        binding?.progress?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progress?.visibility = View.GONE
    }

    override fun createPresenter() {
        mPresenter = MapsPresenter()
    }

    override fun getPresenter(): MapsPresenter {
        return mPresenter
    }
}