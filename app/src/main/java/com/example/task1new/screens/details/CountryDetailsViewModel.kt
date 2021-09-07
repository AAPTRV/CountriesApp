package com.example.task1new.screens.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.task1new.base.mvvm.*
import com.example.domain.dto.CountryDto
import com.example.domain.repository.network.NetworkRepository
import com.example.domain.usecase.impl.GetCountryByNameUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class CountryDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mGetCountryByNameUseCase: GetCountryByNameUseCase
) :
    BaseViewModel(savedStateHandle) {

        private val mCountryLiveData = savedStateHandle.getLiveData<Outcome<CountryDto>>("countryDto")

    fun getCountryLiveData(): MutableLiveData<Outcome<CountryDto>> = mCountryLiveData

    // TODO: Handle progress!
    fun getCountry(mCountryName: String){
        mCountryLiveData.loading(true)
        mCompositeDisposable.add(
            mGetCountryByNameUseCase.setParams(mCountryName).execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({mCountryLiveData.next(it[0])}, {
                    mCountryLiveData.failed(it)}, {
                        if(mCountryLiveData.value is Outcome.Next){
                            mCountryLiveData.success((mCountryLiveData.value as Outcome.Next).data)
                        }
                })
        )
    }
}