package com.example.task1new.di

import androidx.lifecycle.SavedStateHandle
import com.example.task1new.Retrofit
import com.example.task1new.repository.networkRepo.NetworkRepository
import com.example.task1new.repository.networkRepo.NetworkRepositoryImpl
import com.example.task1new.room.DBInfo
import com.example.task1new.screens.countryList.CountryListFragment
import com.example.task1new.screens.countryList.CountryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryListModule = module {

    scope<CountryListFragment> {
        viewModel { (handle: SavedStateHandle) ->
            CountryListViewModel(handle, get(), get())
        }
    }
    //Model level
    single { DBInfo.init(get()) }
    single { Retrofit.COUNTRY_SERVICE }

    //Data level
    single { NetworkRepositoryImpl(get()) as NetworkRepository }


}