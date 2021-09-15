package com.example.task1new.di

import androidx.lifecycle.SavedStateHandle
import com.example.task1new.screens.capitals.CapitalsFragment
import com.example.task1new.screens.capitals.CapitalsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val capitalsModule = module {

    scope<CapitalsFragment> {

        viewModel { (handle: SavedStateHandle) ->
            CapitalsViewModel(handle, get())
        }
    }
}