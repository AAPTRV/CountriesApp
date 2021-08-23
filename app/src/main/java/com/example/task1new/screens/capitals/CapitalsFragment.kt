package com.example.task1new.screens.capitals

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import com.example.task1new.R
import com.example.task1new.databinding.CapitalsFragmentBinding

class CapitalsFragment : Fragment(R.layout.capitals_fragment) {

    companion object {
        fun newInstance() = CapitalsFragment()
    }

    private var binding: CapitalsFragmentBinding? = null
    private val mViewModel: CapitalsViewModel = CapitalsViewModel(SavedStateHandle())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CapitalsFragmentBinding.bind(view)
    }

}