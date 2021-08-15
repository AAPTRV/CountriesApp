package com.example.task1new.di

import androidx.lifecycle.SavedStateHandle
import com.example.task1new.Retrofit
import com.example.task1new.repository.networkRepo.NetworkRepository
import com.example.task1new.repository.networkRepo.NetworkRepositoryImpl
import com.example.task1new.room.DBInfo
import com.example.task1new.screens.details.CountryDetailsFragment
import com.example.task1new.screens.details.CountryDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryDetailsModule = module {

    scope<CountryDetailsFragment> {
        viewModel { (handle: SavedStateHandle) ->
            CountryDetailsViewModel(handle, get(), get())
        }
    }
    //Model level
    single { DBInfo.init(get()) }
    single { Retrofit.COUNTRY_SERVICE }

    //Data level
    single { NetworkRepositoryImpl(get()) as NetworkRepository }

}