package com.example.task1new.screens.details

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import com.example.task1new.COUNTRY_NAME_BUNDLE_KEY
import com.example.task1new.R
import com.example.task1new.base.mvp.BaseMvpFragment
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.content.dialog.CustomDialog
import com.example.task1new.databinding.FragmentCountryDetailsBinding
import com.example.domain.dto.CountryDto
import com.example.task1new.ext.loadSvg
import com.example.task1new.ext.showDialogWithOneButton
import com.example.task1new.ext.showSimpleDialogNetworkError
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.component.getScopeName


private const val SHARED_PREFS: String = "sharedPrefs"
private const val NOTE_TEXT_STATE = "Note text state"

// TODO: Implement MVVM
class CountryDetailsFragment : ScopeFragment(), OnMapReadyCallback {

    private lateinit var mLanguagesAdapter: LanguageAdapter
    private lateinit var mCountryName: String
    private var mGoogleMap: GoogleMap? = null
    private var binding: FragmentCountryDetailsBinding? = null
    private val mViewModel: CountryDetailsViewModel by stateViewModel()


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

        loadNoteTextState()

        mViewModel.getCountry(mCountryName)


        mViewModel.getCountryLiveData().observe(viewLifecycleOwner, {
            when (it) {
                is Outcome.Progress ->{
                    showProgress()
                }
                is Outcome.Next -> {
                    Toast.makeText(
                        this.requireActivity(),
                        "name downloaded (next)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Outcome.Failure -> {
                    Toast.makeText(this.requireActivity(), "Name ERROR", Toast.LENGTH_SHORT).show()
                    hideProgress()
                    showError("Error", it.e)
                }
                is Outcome.Success -> {
                    binding?.countryName?.text = it.data.name
                    showCountryInfo(
                        it.data, LatLng(
                            it.data.convertToLatLngDto().mLatitude,
                            it.data.convertToLatLngDto().mLongitude
                        )
                    )
                    Toast.makeText(this.requireActivity(), "Name SUCCESS", Toast.LENGTH_SHORT)
                        .show()
                    hideProgress()
                }
            }
        })

        binding?.mvCountryDetails?.getMapAsync(this)

        binding?.countryName?.text = mCountryName

        binding?.srCountryDetails?.setOnRefreshListener {
            mViewModel.getCountry(mCountryName)
        }

        mLanguagesAdapter = LanguageAdapter()
        binding?.rvCountryDetailsLanguages?.adapter = mLanguagesAdapter


        // TODO: FILL THE CONTENT
        binding?.note?.setOnClickListener {
            activity?.showDialogWithOneButton(
                null,
                "123",
                R.string.dialog_ok,
                null
            )
        }
    }


    override fun onStart() {
        super.onStart()
        binding?.mvCountryDetails?.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.mvCountryDetails?.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        binding?.mvCountryDetails?.onStop()
    }

    override fun onResume() {
        binding?.mvCountryDetails?.onResume()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mGoogleMap = null
    }

    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mvCountryDetails?.onLowMemory()
    }

    private fun showCountryInfo(country: CountryDto, location: LatLng) {
        mLanguagesAdapter.addListOfItems(
            country.languages
        )
        binding?.srCountryDetails?.isRefreshing = false
        binding?.ivCountryFlag?.loadSvg(
            country.flag
        )
        mGoogleMap?.addMarker(
            MarkerOptions().position(
                location
            )
                .title(country.name)
        )

        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun saveNoteTextState() {
        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences(
            SHARED_PREFS, AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(NOTE_TEXT_STATE, binding?.note?.text.toString())
        editor.apply()
        Toast.makeText(this.requireActivity(), "Data Saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadNoteTextState() {
        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences(
            SHARED_PREFS, AppCompatActivity.MODE_PRIVATE
        )
        binding?.note?.text = sharedPreferences.getString(NOTE_TEXT_STATE, "No note yet")
    }

    fun showError(error: String, throwable: Throwable) {
        activity?.showSimpleDialogNetworkError()
    }

    fun showProgress() {
        binding?.progressDetails?.visibility = View.VISIBLE
    }

    fun hideProgress() {
        binding?.progressDetails?.visibility = View.GONE
    }


}