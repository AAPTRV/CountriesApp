package com.example.task1new.screens.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.task1new.Retrofit
import com.example.task1new.base.mvvm.*
import com.example.task1new.dto.CountryDto
import com.example.task1new.model.convertToCountryDto
import com.example.task1new.repository.networkRepo.NetworkRepository
import com.example.task1new.room.DBInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.coroutineContext

class CountryDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    mDataBase: DBInfo,
    private val mNetworkRepository: NetworkRepository
) :
    BaseViewModel(savedStateHandle) {

    private val mCountryLiveData = savedStateHandle.getLiveData<Outcome<CountryDto>>("countryDto")

    fun changeName() {
        if (mCountryLiveData.value is Outcome.Success) {
            val result = (mCountryLiveData.value as Outcome.Success).data.copy(name = "Name Changed")
            mCountryLiveData.success(result)
        }
    }

    fun getCountryLiveData(): MutableLiveData<Outcome<CountryDto>> = mCountryLiveData

    // TODO: Handle progress!
    fun getCountry(mCountryName: String) {
        mCountryLiveData.loading(true)
        mCompositeDisposable.add(
            mNetworkRepository.getCountryByName(mCountryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mCountryLiveData.next(it[0]) }, {
                    mCountryLiveData.failed(it)
                }, {
                    if (mCountryLiveData.value is Outcome.Next) {
                        mCountryLiveData.success((mCountryLiveData.value as Outcome.Next).data)
                    }
                })
        )
    }

    fun getCountryCoroutines(countryName: String) {
        CoroutineScope(viewModelScope.coroutineContext).launch {
            try {
                mCountryLiveData.value = Outcome.loading(true)
                val result = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
                    Retrofit.COROUTINE_COUNTRY_SERVICE.getCountryByName(countryName)
                }
                mCountryLiveData.value = Outcome.loading(false)
                mCountryLiveData.value = Outcome.success(result.convertToCountryDto()[0])
            } catch (e: Exception) {
                mCountryLiveData.value = Outcome.loading(false)
                mCountryLiveData.value = Outcome.failure(e)
            }
        }

    }

}