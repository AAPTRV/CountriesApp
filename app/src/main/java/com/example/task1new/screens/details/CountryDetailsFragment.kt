package com.example.task1new.screens.details

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.task1new.COUNTRY_NAME_BUNDLE_KEY
import com.example.task1new.OkRetrofit
import com.example.task1new.R
import com.example.task1new.base.mvp.BaseMvpFragment
import com.example.task1new.content.dialog.CustomDialog
import com.example.task1new.databinding.FragmentCountryDetailsBinding
import com.example.task1new.dto.PostCountryItemDto
import com.example.task1new.ext.loadSvg
import com.example.task1new.ext.showSimpleDialogNetworkError
import com.example.task1new.model.PostCountryItemModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.custom_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val SHARED_PREFS: String = "sharedPrefs"
private const val NOTE_TEXT_STATE = "Note text state"

class CountryDetailsFragment : BaseMvpFragment <CountryDetailsView, CountryDetailsPresenter>(), OnMapReadyCallback, CountryDetailsView {

    private lateinit var mLanguagesAdapter: LanguageAdapter
    private lateinit var mCountryName: String
    private lateinit var mGoogleMap: GoogleMap
    private var binding: FragmentCountryDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("hz", "COUNTRY DETAILS FRAGMENT -> onCreateView")
        binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        mCountryName = arguments?.getString(COUNTRY_NAME_BUNDLE_KEY, "") ?: ""
        binding?.mvCountryDetails?.onCreate(savedInstanceState)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("hz", "COUNTRY DETAILS FRAGMENT -> onViewCreated")

        getPresenter().attachView(this)
        loadNoteTextState()

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
        val myDialog = CustomDialog(this.requireContext())
        myDialog.create()
        binding?.note?.setOnClickListener{
            myDialog.show()
        }
        val mEtDialog = myDialog.findViewById<EditText>(R.id.editText)
        mEtDialog.requestFocus()
        mEtDialog.showSoftInputOnFocus = true
        val mOkButton = myDialog.findViewById<Button>(R.id.button_ok)
        val mCancelButton = myDialog.findViewById<Button>(R.id.button_cancel)
        mOkButton.setOnClickListener {
            binding?.note?.text = mEtDialog.text.toString()
            saveNoteTextState()
            myDialog.dismiss()
        }
        mCancelButton.setOnClickListener {
            myDialog.dismiss()
        }
    }

    private fun getCountryByName(isRefresh: Boolean) {
        binding?.progress?.visibility = if (isRefresh) View.GONE else View.VISIBLE
        OkRetrofit.jsonPlaceHolderApi.getCountryByName(mCountryName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({response ->
                Log.e("hz", response.toString())
                mLanguagesAdapter.addListOfItems(
                    response[0].convertToPostCountryItemDto().languages

                )
                binding?.srCountryDetails?.isRefreshing = false
                binding?.ivCountryFlag?.loadSvg(
                    response[0].flag.toString()
                )

                val position = LatLng(
                    response[0].convertToLatLngDto().mLatitude,
                    response[0].convertToLatLngDto().mLongitude
                )

                mGoogleMap.addMarker(
                    MarkerOptions().position(
                        position
                    )
                )
                val cameraPosition = CameraPosition.Builder().target(position).build()
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                binding?.progress?.visibility = View.GONE

            }, {throwable -> throwable.printStackTrace()
                binding?.srCountryDetails?.isRefreshing = false
                binding?.progress?.visibility = View.GONE
                activity?.showSimpleDialogNetworkError()})
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.e("hz", "COUNTRY DETAILS FRAGMENT -> onMapReady")
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )
    }

    override fun onResume() {
        Log.e("hz", "COUNTRY DETAILS FRAGMENT -> onResume")
        binding?.mvCountryDetails?.onResume()
        super.onResume()
    }

    override fun onDestroyView() {
        Log.e("hz", "COUNTRY DETAILS FRAGMENT -> onDestroyView")
        super.onDestroyView()
        binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mvCountryDetails?.onLowMemory()
    }

    private fun saveNoteTextState(){
        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences(
            SHARED_PREFS, AppCompatActivity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(NOTE_TEXT_STATE, binding?.note?.text.toString())
        editor.apply()
        Toast.makeText(this.requireActivity(), "Data Saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadNoteTextState(){
        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences(
            SHARED_PREFS, AppCompatActivity.MODE_PRIVATE)
        binding?.note?.text = sharedPreferences.getString(NOTE_TEXT_STATE,"No note yet")
    }

    override fun createPresenter() {
        mPresenter = CountryDetailsPresenter()
    }

    override fun getPresenter(): CountryDetailsPresenter {
        return mPresenter
    }

    override fun showCountryInfo(country: PostCountryItemDto, location: LatLng) {
        TODO("Not yet implemented")
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

}