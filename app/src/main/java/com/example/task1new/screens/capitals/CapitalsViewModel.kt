package com.example.task1new.screens.capitals

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.SingleCapitalDto
import com.example.domain.repository.network.NetworkRepositoryCoroutines
import com.example.task1new.base.mvvm.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CapitalsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mNetworkRepositoryCoroutines: NetworkRepositoryCoroutines
) : BaseViewModel(savedStateHandle) {
    private val mCapitalsListLiveData =
        savedStateHandle.getLiveData<Outcome<List<SingleCapitalDto>>>("capitals")

    fun getCapitalsLiveData(): MutableLiveData<Outcome<List<SingleCapitalDto>>>{
        return mCapitalsListLiveData
    }

    fun getCapitalsCoroutines() {
        Log.e("HZ", "We are in ${Thread.currentThread().name} thread ... (funGetCapitals)")
        mCapitalsListLiveData.loading(true)
        viewModelScope.launch {
            try{
                val capitalsList = withContext(Dispatchers.IO){
                    Log.e("HZ", "We are in ${Thread.currentThread().name} thread ... (viewModelScope)")
                    mNetworkRepositoryCoroutines.getCapitalsListCoroutines()
                }
                Log.e("HZ", "CAPITAL LIST SIZE = ${capitalsList.size}")
                mCapitalsListLiveData.success(capitalsList)
            } catch (exception: Exception){
                Log.e("HZ", "EXCEPTION: ${exception.toString()}")
                mCapitalsListLiveData.failed(exception)
            }
        }
    }
}