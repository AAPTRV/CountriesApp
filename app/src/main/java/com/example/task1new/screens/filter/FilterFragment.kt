package com.example.task1new.screens.filter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.task1new.*
import com.example.task1new.base.filter.BaseFilterFragment
import com.example.task1new.databinding.FragmentFilterBinding
import com.example.task1new.screens.countryList.CountryListAdapter
import com.example.task1new.screens.countryList.CountryListFragment
import com.google.android.material.slider.RangeSlider
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FilterFragment : Fragment() {

    // TODO: Rename and change types of parameters

    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentFilterBinding? = null

    private var mCurrentMinArea by Delegates.notNull<Float>()
    private var mCurrentMaxArea by Delegates.notNull<Float>()

    private var mCurrentMinPopulation by Delegates.notNull<Float>()
    private var mCurrentMaxPopulation by Delegates.notNull<Float>()

    private var mCurrentMinDistance by Delegates.notNull<Float>()
    private var mCurrentMaxDistance by Delegates.notNull<Float>()

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
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        val mMinArea = arguments?.getString(ADAPTER_MINIMUM_AREA_BUNDLE_KEY, "")?.toFloat() ?: 0.0F
        val mMaxArea = arguments?.getString(ADAPTER_MAXIMUM_AREA_BUNDLE_KEY, "")?.toFloat() ?: 0.0F

        binding?.areaSlider?.valueFrom = mMinArea
        binding?.areaSlider?.valueTo = mMaxArea
        binding?.areaSlider?.stepSize = getStepSize(mMinArea, mMaxArea)
        binding?.areaSlider?.values = mutableListOf(mMinArea, mMaxArea)

        val mMinPopulation =
            arguments?.getString(ADAPTER_MINIMUM_POPULATION_BUNDLE_KEY, "")?.toFloat() ?: 0.0F
        val mMaxPopulation =
            arguments?.getString(ADAPTER_MAXIMUM_POPULATION_BUNDLE_KEY, "")?.toFloat() ?: 0.0F

        binding?.populationSlider?.valueFrom = mMinPopulation
        binding?.populationSlider?.valueTo = mMaxPopulation
        binding?.populationSlider?.stepSize = getStepSize(mMinPopulation, mMaxPopulation)
        binding?.populationSlider?.values = mutableListOf(mMinPopulation, mMaxPopulation)

        val mMinDistance =
            arguments?.getString(ADAPTER_MINIMUM_DISTANCE_BUNDLE_KEY, "")?.toFloat() ?: 0.0F
        val mMaxDistance =
            arguments?.getString(ADAPTER_MAXIMUM_DISTANCE_BUNDLE_KEY, "")?.toFloat() ?: 0.0F

        binding?.distanceSlider?.valueFrom = mMinDistance
        binding?.distanceSlider?.valueTo = mMaxDistance
        binding?.distanceSlider?.stepSize = getStepSize(mMinDistance, mMaxDistance)
        binding?.distanceSlider?.values = mutableListOf(mMinDistance, mMaxDistance)

        binding?.areaSlider?.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                mCurrentMaxArea = slider.values[1]
                mCurrentMinArea = slider.values[0]
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                mCurrentMaxArea = slider.values[1]
                mCurrentMinArea = slider.values[0]
            }
        })

        binding?.populationSlider?.addOnSliderTouchListener(object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                mCurrentMaxPopulation = slider.values[1]
                mCurrentMinPopulation = slider.values[0]
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                mCurrentMaxPopulation = slider.values[1]
                mCurrentMinPopulation = slider.values[0]
            }
        })

        binding?.distanceSlider?.addOnSliderTouchListener(object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                mCurrentMaxDistance = slider.values[1]
                mCurrentMinDistance = slider.values[0]
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                mCurrentMaxDistance = slider.values[1]
                mCurrentMinDistance = slider.values[0]
            }
        })

        return binding?.root
    }

    private fun getStepSize(min: Float, max: Float, numberOfDividers: Int = 10): Float {
        return (max - min) / numberOfDividers
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FilterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FilterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}