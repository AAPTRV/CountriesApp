package com.example.task1new.di

import androidx.lifecycle.SavedStateHandle
import com.example.task1new.screens.countryList.CountryListFragment
import com.example.task1new.screens.countryList.CountryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryListModule = module {

    scope<CountryListFragment> {
        viewModel { (handle: SavedStateHandle) ->
            CountryListViewModel(handle, get(), get(), get(), get())
        }
    }
}