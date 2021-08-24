package com.example.task1new.screens.capitals

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.ext.showSimpleDialogNetworkError
import com.example.task1new.R
import com.example.task1new.base.mvvm.BaseMvvmView
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.databinding.CapitalsFragmentBinding
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class CapitalsFragment : ScopeFragment(R.layout.capitals_fragment), BaseMvvmView {

    companion object {
        fun newInstance() = CapitalsFragment()
    }

    private var myAdapter = CapitalsAdapter()
    private var binding: CapitalsFragmentBinding? = null
    private val mViewModel: CapitalsViewModel by stateViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CapitalsFragmentBinding.bind(view)
        binding?.rvCountryCapitals?.layoutManager = LinearLayoutManager(context)
        binding?.rvCountryCapitals?.adapter = myAdapter
        mViewModel.getCapitalsCoroutines()

        mViewModel.getCapitalsLiveData().observe(viewLifecycleOwner, { outcome ->
            when(outcome){
                is Outcome.Progress -> {
                    showProgress()
                }
                is Outcome.Next -> {
                    hideProgress()
                }
                is Outcome.Success -> {
                    hideProgress()
                    myAdapter.repopulateAdapterData(outcome.data)
                }
                is Outcome.Failure -> {
                    hideProgress()
                }
            }
        })
    }

    override fun showError(error: String, throwable: Throwable) {
        activity?.showSimpleDialogNetworkError()
    }

    override fun showProgress() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressBar?.visibility = View.GONE
    }
}