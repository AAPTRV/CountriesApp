package com.example.task1new.screens.capitals

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.ext.showSimpleDialogNetworkError
import com.example.task1new.R
import com.example.task1new.base.mvvm.BaseMvvmView
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.databinding.CapitalsFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class CapitalsFragment : ScopeFragment(R.layout.capitals_fragment), BaseMvvmView {

    private var myAdapter = CapitalsAdapter()
    private var binding: CapitalsFragmentBinding? = null
    private val mViewModel: CapitalsViewModel by stateViewModel()

    private lateinit var mSearchButton: MenuItem
    private lateinit var mMapsButton: MenuItem

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.capitals_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        mSearchButton = menu.findItem(R.id.capitals_menu_search_button)
        mMapsButton = menu.findItem(R.id.capitals_menu_maps_button)

        val mSvMenu = mSearchButton.actionView as SearchView

        mSvMenu.setOnSearchClickListener {
            mMapsButton.isVisible = false
        }

        mSvMenu.setOnCloseListener {
            mMapsButton.isVisible = true
            false
        }

        mSvMenu.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { mViewModel.setValueInSearchStateFlow(query) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { mViewModel.setValueInSearchStateFlow(newText) }
                return true
            }
        }
        )

        mSvMenu.setOnCloseListener {
            mViewModel.getFlowCapitalsListFromApi()
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CapitalsFragmentBinding.bind(view)
        binding?.rvCountryCapitals?.layoutManager = LinearLayoutManager(context)
        binding?.rvCountryCapitals?.adapter = myAdapter

        setHasOptionsMenu(true)

        mViewModel.getFlowCapitalsListFromApi().asLiveData()
            .observe(viewLifecycleOwner, { outcome ->
                when (outcome) {
                    is Outcome.Progress -> {
                        if (outcome.loading) {
                            showProgress()
                        } else {
                            hideProgress()
                        }
                    }
                    is Outcome.Next -> {
                        myAdapter.repopulateAdapterData(outcome.data)
                    }
                    is Outcome.Success -> {
                        myAdapter.repopulateAdapterData(outcome.data)
                    }
                    is Outcome.Failure -> {
                        showError(outcome.e.toString(), outcome.e)
                    }
                }
            })

        CoroutineScope(lifecycleScope.coroutineContext).launch {
            mViewModel.setUpSearchItem().asLiveData().observe(viewLifecycleOwner, { outcome ->
                when (outcome) {
                    is Outcome.Progress -> {
                        if (outcome.loading) {
                            showProgress()
                        } else {
                            hideProgress()
                        }
                    }
                    is Outcome.Next -> {
                        myAdapter.repopulateAdapterData(outcome.data)
                    }
                    is Outcome.Success -> {
                        myAdapter.repopulateAdapterData(outcome.data)
                    }
                    is Outcome.Failure -> {
                        showError(outcome.e.toString(), outcome.e)
                    }
                }
            })
        }
//        mViewModel.getCapitalsLiveData().observe(viewLifecycleOwner, { outcome ->
//            when (outcome) {
//                is Outcome.Progress -> {
//                    if (outcome.loading) {
//                        showProgress()
//                    } else {
//                        hideProgress()
//                    }
//                }
//                is Outcome.Next -> {
//                    myAdapter.repopulateAdapterData(outcome.data)
//                }
//                is Outcome.Success -> {
//                    myAdapter.repopulateAdapterData(outcome.data)
//                }
//                is Outcome.Failure -> {
//                    showError(outcome.e.toString(), outcome.e)
//                }
//            }
//        })
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