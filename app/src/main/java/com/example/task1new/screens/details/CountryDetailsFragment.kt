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
import com.example.task1new.databinding.FragmentCountryDetailsBinding
import com.example.task1new.dto.LatLngDto
import com.example.task1new.ext.loadSvg
import com.example.task1new.ext.showDialogWithOneButton
import com.example.task1new.ext.showSimpleDialog
import com.example.task1new.model.PostCountryItemModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryDetailsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mLanguagesAdapter: LanguageAdapter
    private lateinit var mCountryName: String
    private lateinit var mGoogleMap: GoogleMap
    private var binding: FragmentCountryDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        mCountryName = arguments?.getString(COUNTRY_NAME_BUNDLE_KEY, "") ?: ""
        binding?.mvCountryDetails?.onCreate(savedInstanceState)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.mvCountryDetails?.getMapAsync(OnMapReadyCallback {
            mGoogleMap = it
        })

        binding?.countryName?.text = mCountryName

        mLanguagesAdapter = LanguageAdapter()
        binding?.rvCountryDetailsLanguages?.adapter = mLanguagesAdapter

        binding?.srCountryDetails?.setOnRefreshListener {
            getCountryByName(true)
        }
        getCountryByName(false)
        activity?.showSimpleDialog()
    }

    private fun getCountryByName(isRefresh: Boolean) {
        binding?.progress?.visibility = if (isRefresh) View.GONE else View.VISIBLE
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
                    binding?.srCountryDetails?.isRefreshing = false
                    binding?.ivCountryFlag?.loadSvg(
                        response.body()?.get(0)?.flagImageUrl.toString()
                    )

                    val position = LatLng(
                        response.body()?.get(0)?.convertToLatLngDto()?.mLatitude!!,
                        response.body()?.get(0)?.convertToLatLngDto()?.mLongitude!!
                    )

                    mGoogleMap.addMarker(
                        MarkerOptions().position(
                            position
                        )
                    )
                    val cameraPosition = CameraPosition.Builder().target(position).build()
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    binding?.progress?.visibility = View.GONE
                }

                override fun onFailure(call: Call<List<PostCountryItemModel>>, t: Throwable) {
                    t.printStackTrace()
                    binding?.srCountryDetails?.isRefreshing = false
                    binding?.progress?.visibility = View.GONE
                }

            })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )
    }

    override fun onResume() {
        binding?.mvCountryDetails?.onResume()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mvCountryDetails?.onLowMemory()
    }

}