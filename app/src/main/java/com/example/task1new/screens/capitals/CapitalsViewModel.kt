package com.example.task1new.screens.capitals

import android.util.Log
import androidx.lifecycle.*
import com.example.data.utils.NetConstants.MIN_QUERY_LENGTH
import com.example.data.utils.NetConstants.SEARCH_DELAY_MILLIS
import com.example.domain.dto.SingleCapitalDto
import com.example.domain.repository.network.NetworkRepositoryCoroutines
import com.example.domain.repository.network.NetworkRepositoryFlow
import com.example.task1new.base.mvvm.*
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CapitalsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mNetworkRepositoryCoroutines: NetworkRepositoryCoroutines,
    private val mNetworkRepositoryFlow: NetworkRepositoryFlow
) : BaseViewModel(savedStateHandle) {
    private val mCapitalsListLiveData =
        savedStateHandle.getLiveData<Outcome<List<SingleCapitalDto>>>("capitals")

    fun getCapitalsFlowLiveData(): LiveData<Outcome<List<SingleCapitalDto>>>{
        return mCapitals
    }

    private val mCapitals: LiveData<Outcome<List<SingleCapitalDto>>> = mNetworkRepositoryFlow
        .getCapitalsListFlow()
        .map { list -> Outcome.success(list) }
        .onStart { emit(Outcome.loading(true)) }
        .onCompletion { emit(Outcome.loading(false)) }
        .catch { exception ->
            emit(Outcome.failure(exception))
            println("SOME ERROR DURING GET CAPITALS LIST FLOW")
        }
        .asLiveData()


    fun getCapitalsLiveData(): MutableLiveData<Outcome<List<SingleCapitalDto>>> {
        return mCapitalsListLiveData
    }


    fun getCapitalsCoroutines() {
        Log.e("HZ", "We are in ${Thread.currentThread().name} thread ... (funGetCapitals)")
        mCapitalsListLiveData.loading(true)
        viewModelScope.launch {
            try {
                val capitalsList = withContext(Dispatchers.IO) {
                    Log.e(
                        "HZ",
                        "We are in ${Thread.currentThread().name} thread ... (viewModelScope)"
                    )
                    mNetworkRepositoryCoroutines.getCapitalsListCoroutines()
                }
                Log.e("HZ", "CAPITAL LIST SIZE = ${capitalsList.size}")
                mCapitalsListLiveData.success(capitalsList)
            } catch (exception: Exception) {
                Log.e("HZ", "EXCEPTION: ${exception.toString()}")
                mCapitalsListLiveData.failed(exception)
            }
        }
    }
}