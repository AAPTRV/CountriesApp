package com.example.task1new.screens.capitals

import android.util.Log
import androidx.lifecycle.*
import com.example.data.utils.NetConstants.MIN_QUERY_LENGTH
import com.example.data.utils.NetConstants.SEARCH_DELAY_MILLIS
import com.example.domain.dto.SingleCapitalDto
import com.example.domain.repository.network.NetworkRepositoryCoroutines
import com.example.domain.repository.network.NetworkRepositoryFlow
import com.example.task1new.base.mvvm.*
import com.example.task1new.ext.modifyFlowToOutcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CapitalsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mNetworkRepositoryCoroutines: NetworkRepositoryCoroutines,
    private val mNetworkRepositoryFlow: NetworkRepositoryFlow
) : BaseViewModel(savedStateHandle) {

    private val mSearchStateFlow = MutableStateFlow("")
    private val mCapitals = MutableLiveData<Outcome<List<SingleCapitalDto>>>()

    fun getCapitalsLiveData(): MutableLiveData<Outcome<List<SingleCapitalDto>>> {
        return mCapitals
    }

    fun setValueInSearchStateFlow(value: String) {
        mSearchStateFlow.value = value
    }

    fun getFlowCapitalsListFromApi(): Flow<Outcome<List<SingleCapitalDto>>> =
        mNetworkRepositoryFlow.getCapitalsListFlow().modifyFlowToOutcome()

    fun getSearchItem() {
        viewModelScope.launch {
            mSearchStateFlow
                .filter { it.length >= MIN_QUERY_LENGTH }
                .debounce(SEARCH_DELAY_MILLIS)
                .distinctUntilChanged()
                .flatMapLatest {
                    mNetworkRepositoryFlow.getCapitalsByNameFlow(it).modifyFlowToOutcome()
                }
                .flowOn(Dispatchers.IO)
                .onEach {
                    mCapitals.value = it as Outcome<List<SingleCapitalDto>>
                    Log.e("HZ", "GET SEARCH ITEM END")
                }
                .catch { emitAll(flowOf()) }
                .collect {}
        }
    }
}