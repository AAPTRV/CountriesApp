package com.example.task1new.screens.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.Navigation
import com.example.task1new.*
import com.example.task1new.databinding.FragmentFilterBinding
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider

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

        return binding?.root
    }

    private fun getStepSize(min: Float, max: Float, numberOfDividers: Int = 10): Float {
        return (max - min) / numberOfDividers
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mMinArea = arguments?.getString(ADAPTER_MINIMUM_AREA_BUNDLE_KEY, "")?.toFloat() ?: 0.0F
        var mMaxArea = arguments?.getString(ADAPTER_MAXIMUM_AREA_BUNDLE_KEY, "")?.toFloat() ?: 0.0F
        binding?.areaSlider?.valueFrom = mMinArea
        binding?.areaSlider?.valueTo = mMaxArea
        binding?.areaSlider?.stepSize = getStepSize(mMinArea, mMaxArea)
        binding?.areaSlider?.values = mutableListOf(mMinArea, mMaxArea)

        var mMinPopulation =
            arguments?.getString(ADAPTER_MINIMUM_POPULATION_BUNDLE_KEY, "")?.toFloat() ?: 0.0F
        var mMaxPopulation =
            arguments?.getString(ADAPTER_MAXIMUM_POPULATION_BUNDLE_KEY, "")?.toFloat() ?: 0.0F
        binding?.populationSlider?.valueFrom = mMinPopulation
        binding?.populationSlider?.valueTo = mMaxPopulation
        binding?.populationSlider?.stepSize = getStepSize(mMinPopulation, mMaxPopulation)
        binding?.populationSlider?.values = mutableListOf(mMinPopulation, mMaxPopulation)

//        var mMinDistance =
//            arguments?.getString(ADAPTER_MINIMUM_DISTANCE_BUNDLE_KEY, "")?.toFloat() ?: 0.0F
        var mMaxDistance =
            arguments?.getString(ADAPTER_MAXIMUM_DISTANCE_BUNDLE_KEY, "")?.toFloat() ?: 0.0F

        binding?.distanceSlider?.valueFrom = 0F
        binding?.distanceSlider?.valueTo = mMaxDistance
        binding?.distanceSlider?.stepSize = getStepSize(0F, mMaxDistance)
//        binding?.distanceSlider?.values = mutableListOf(mMinDistance, mMaxDistance)

        binding?.areaTextView?.text =
            context?.getString(R.string.filter_area, mMinArea / 1000, mMaxArea / 1000)
        binding?.populationTextView?.text =
            context?.getString(
                R.string.filter_population,
                mMinPopulation / 1000000,
                mMaxPopulation / 1000000
            )
        binding?.distanceTextView?.text =
            context?.getString(R.string.filter_distance, mMaxDistance)

        binding?.areaSlider?.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                mMaxArea = slider.values[1]
                mMinArea = slider.values[0]
                binding?.areaTextView?.text =
                    context?.getString(
                        R.string.filter_area,
                        mMinArea / 1000,
                        mMaxArea / 1000
                    )
            }
        })

        binding?.populationSlider?.addOnSliderTouchListener(object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                mMaxPopulation = slider.values[1]
                mMinPopulation = slider.values[0]
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                mMaxPopulation = slider.values[1]
                mMinPopulation = slider.values[0]
                binding?.populationTextView?.text =
                    context?.getString(
                        R.string.filter_population,
                        mMinPopulation / 1000000,
                        mMaxPopulation / 1000000
                    )
            }
        })

        binding?.distanceSlider?.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                mMaxDistance = slider.value
            }

            override fun onStopTrackingTouch(slider: Slider) {
                mMaxDistance = slider.value
                binding?.distanceTextView?.text =
                    context?.getString(
                        R.string.filter_distance,
                        mMaxDistance
                    )
            }
        })

        binding?.filterFragmentButton?.setOnClickListener {

            val result = mutableListOf<Double>()
            result.add(mMaxArea.toDouble())
            result.add(mMinArea.toDouble())
            result.add(mMaxPopulation.toDouble())
            result.add(mMinPopulation.toDouble())
            result.add(mMaxDistance.toDouble())

            setFragmentResult("filterKey", bundleOf("resultList" to result))

            Navigation.findNavController(requireView())
                .navigate(R.id.action_filterFragment_to_blankFragmentRV)
        }
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