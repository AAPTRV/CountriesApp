package com.example.task1new.screens.capitals

import android.util.Log
import androidx.lifecycle.*
import com.example.data.utils.NetConstants.MIN_QUERY_LENGTH
import com.example.data.utils.NetConstants.SEARCH_DELAY_MILLIS
import com.example.domain.dto.SingleCapitalDto
import com.example.domain.repository.network.NetworkRepositoryFlow
import com.example.task1new.base.mvvm.*
import com.example.task1new.ext.modifyFlowToOutcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CapitalsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mNetworkRepositoryFlow: NetworkRepositoryFlow
) : BaseViewModel(savedStateHandle) {

    private val mSearchStateFlow = MutableStateFlow("")
    val mNavigateSharedFlow: MutableSharedFlow<Long> = MutableSharedFlow()

    fun updateSearchStateFlow(value: String) {
        mSearchStateFlow.value = value
    }

    private fun getCapitalsListFlow() =
        mNetworkRepositoryFlow.getCapitalsListFlow()
            .modifyFlowToOutcome()

    private fun getCapitalsByNameFlow(name: String) =
        mNetworkRepositoryFlow.getCapitalsByNameFlow(name).modifyFlowToOutcome()

    fun getSearchLiveData() =
        mSearchStateFlow
            .filter { it.length >= MIN_QUERY_LENGTH || it == "" }
            .debounce(SEARCH_DELAY_MILLIS)
            .distinctUntilChanged()
            .flatMapLatest {
                if(it.isEmpty()){
                    println("HZ before GetListFlow Current thread is ${Thread.currentThread()}")
                    getCapitalsListFlow()
                } else {
                    println("HZ before GetListName Current thread is ${Thread.currentThread()}")
                    getCapitalsByNameFlow(it)
                }
            }
            .flowOn(Dispatchers.IO)
            .catch { emitAll(flowOf()) }
            .asLiveData()
}