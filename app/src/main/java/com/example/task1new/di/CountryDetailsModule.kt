package com.example.task1new.di

import androidx.lifecycle.SavedStateHandle
import com.example.domain.usecase.impl.GetCountryByNameUseCase
import com.example.task1new.screens.details.CountryDetailsFragment
import com.example.task1new.screens.details.CountryDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryDetailsModule = module {

    scope<CountryDetailsFragment> {
        scoped { GetCountryByNameUseCase(get()) }
        viewModel { (handle: SavedStateHandle) ->
            CountryDetailsViewModel(handle, get())
        }
    }
}