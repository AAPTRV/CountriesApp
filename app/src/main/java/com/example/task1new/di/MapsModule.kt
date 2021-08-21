package com.example.task1new.di

import androidx.lifecycle.SavedStateHandle
import com.example.domain.usecase.impl.GetAllCountriesUseCase
import com.example.task1new.screens.map.MapsFragmentBlank
import com.example.task1new.screens.map.MapsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapsModule = module {


    scope<MapsFragmentBlank> {

        scoped { GetAllCountriesUseCase(get()) }

        viewModel { (handle: SavedStateHandle) ->
            MapsFragmentViewModel(handle, get())
        }
    }
}