package com.example.task1new.screens.countryList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.task1new.Retrofit
import com.example.task1new.base.mvvm.BaseViewModel
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.base.mvvm.executeJob
import com.example.task1new.dto.CountryDto

class CountryListViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel(savedStateHandle) {

    private val mCountryLiveData = savedStateHandle.getLiveData<Outcome<CountryDto>>("countryDto")

    fun getLiveData(): MutableLiveData<Outcome<CountryDto>>{
        return mCountryLiveData
    }

    fun getCountryByName() {
        mCompositeDisposable.add(
            executeJob(
                Retrofit.jsonPlaceHolderApi.getCountryByName(
                    "belarus"
                )
                    .map { it[0].convertToCountryItemDto() }, mCountryLiveData
            )
        )
    }
}