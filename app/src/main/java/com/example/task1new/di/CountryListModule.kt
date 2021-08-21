package com.example.task1new.di

import androidx.lifecycle.SavedStateHandle
import com.example.domain.usecase.impl.GetAllCountriesUseCase
import com.example.domain.usecase.impl.GetCountriesCommonInfoUseCase
import com.example.domain.usecase.impl.GetCountriesLanguagesInfoUseCase
import com.example.task1new.screens.countryList.CountryListFragment
import com.example.task1new.screens.countryList.CountryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryListModule = module {


    scope<CountryListFragment> {

        scoped { GetAllCountriesUseCase(get()) }
        scoped { GetCountriesLanguagesInfoUseCase(get()) }
        scoped { GetCountriesCommonInfoUseCase(get()) }

        viewModel { (handle: SavedStateHandle) ->
            CountryListViewModel(handle, get(), get(), get(), get(), get())
        }
    }
}