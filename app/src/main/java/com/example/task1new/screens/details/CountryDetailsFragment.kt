package com.example.task1new.screens.details

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task1new.COUNTRY_NAME_BUNDLE_KEY
import com.example.task1new.R
import com.example.task1new.base.mvp.BaseMvpFragment
import com.example.task1new.content.dialog.CustomDialog
import com.example.task1new.databinding.FragmentCountryDetailsBinding
import com.example.task1new.dto.PostCountryItemDto
import com.example.task1new.ext.loadSvg
import com.example.task1new.ext.showSimpleDialogNetworkError
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


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
            getPresenter().getCountryByName(mCountryName,true)
        }
        getPresenter().getCountryByName(mCountryName, false)

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

    override fun onMapReady(googleMap: GoogleMap) {

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
        mLanguagesAdapter.addListOfItems(
            country.languages
        )
        binding?.srCountryDetails?.isRefreshing = false
        binding?.ivCountryFlag?.loadSvg(
            country.flag
        )
        mGoogleMap.addMarker(
            MarkerOptions().position(
                location
            )
                .title(country.name)
        )

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        hideProgress()
    }

    override fun showError(error: String, throwable: Throwable) {
        activity?.showSimpleDialogNetworkError()
    }

    override fun showProgress() {
        binding?.progressDetails?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressDetails?.visibility = View.GONE
    }

}