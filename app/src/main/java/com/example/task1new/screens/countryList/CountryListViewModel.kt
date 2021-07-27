package com.example.task1new.screens.countryList

import androidx.lifecycle.*
import com.example.task1new.OkRetrofit
import com.example.task1new.base.mvvm.BaseViewModel
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.base.mvvm.executeJob
import com.example.task1new.dto.PostCountryItemDto

class CountryListViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel(savedStateHandle) {

    val mCountryLiveData = savedStateHandle.getLiveData<Outcome<PostCountryItemDto>>("countryDto")

    fun getCountryByName() {
        mCompositeDisposable.add(
            executeJob(
                OkRetrofit.jsonPlaceHolderApi.getCountryByName("belarus")
                    .map { it[0].convertToPostCountryItemDto() }, mCountryLiveData
            )
        )
    }

    fun getFilteredData(): LiveData<String> = savedStateHandle.getLiveData<String>("query").switchMap { query ->
        MutableLiveData()//repository.getFilteredData(query)
    }

    fun setQuery(query: String) {
        savedStateHandle["countryName"] = query
    }

    fun setCountryName(name: String) {
        savedStateHandle["countryName"] = name
    }

    fun setPostCountryItemDto(dto: PostCountryItemDto) {
        savedStateHandle["countryDto"] = dto
    }

    fun getPostCountryItemDto(): Outcome<PostCountryItemDto>? =
        savedStateHandle["countryDto"]

}