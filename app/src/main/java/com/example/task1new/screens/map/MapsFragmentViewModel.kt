package com.example.task1new.screens.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.data.model.convertToLatLngDto
import com.example.domain.dto.LatLngDto
import com.example.domain.usecase.impl.GetAllCountriesUseCase
import com.example.task1new.base.mvvm.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MapsFragmentViewModel(
    savedStateHandle: SavedStateHandle,
    private val mGetAllCountriesUseCase: GetAllCountriesUseCase
) : BaseViewModel(savedStateHandle) {

    private val mCountriesLatLngListDto =
        savedStateHandle.getLiveData<Outcome<List<LatLngDto>>>("countryDto")

    fun getCountriesLatLngDto(): MutableLiveData<Outcome<List<LatLngDto>>> {
        return mCountriesLatLngListDto
    }

    fun getLatLngDtoFromApi() {
        mCountriesLatLngListDto.loading(true)
        mCompositeDisposable.add(
            mGetAllCountriesUseCase.execute()
                .map { it.convertToLatLngDto() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mCountriesLatLngListDto.next(it)
                }, { }, {
                    if (mCountriesLatLngListDto.value is Outcome.Next) {
                        mCountriesLatLngListDto.success((mCountriesLatLngListDto.value as Outcome.Next).data)
                    }
                }
                )
        )
    }
}