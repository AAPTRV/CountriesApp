package com.example.task1new.screens.capitals

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.ext.showSimpleDialogNetworkError
import com.example.task1new.R
import com.example.task1new.base.mvvm.BaseMvvmView
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.databinding.CapitalsFragmentBinding
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class CapitalsFragment : ScopeFragment(R.layout.capitals_fragment), BaseMvvmView {

    private var myAdapter = CapitalsAdapterDiff()
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
                query?.let { mViewModel.updateSearchStateFlow(query) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { mViewModel.updateSearchStateFlow(newText) }
                return true
            }
        }
        )

        mSvMenu.setOnCloseListener {
            mViewModel.updateSearchStateFlow("")
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CapitalsFragmentBinding.bind(view)
        binding?.rvCountryCapitals?.layoutManager = LinearLayoutManager(context)
        binding?.rvCountryCapitals?.adapter = myAdapter

        setHasOptionsMenu(true)
        mViewModel.getSearchLiveData().observe(viewLifecycleOwner, { outcome ->
            when (outcome) {
                is Outcome.Progress -> {
                    if (outcome.loading) {
                        showProgress()
                    } else {
                        hideProgress()
                    }
                }
                is Outcome.Next -> {
                    myAdapter.submitList(outcome.data.toMutableList())
                }
                is Outcome.Success -> {
                    myAdapter.submitList(outcome.data.toMutableList())
                }
                is Outcome.Failure -> {
                    showError(outcome.e.toString(), outcome.e)
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