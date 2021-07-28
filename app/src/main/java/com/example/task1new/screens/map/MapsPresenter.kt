package com.example.task1new.screens.map

import android.content.ContentValues
import android.util.Log
import com.example.task1new.OkRetrofit
import com.example.task1new.base.mvp.BaseMvpPresenter
import com.example.task1new.dto.LatLngDto

class MapsPresenter : BaseMvpPresenter<MapsView>() {
    fun getDataFromRetrofitToShowMarkers() {
        getView()?.showProgress()
        addDisposable(
            inBackground(
                OkRetrofit.jsonPlaceHolderApi.getPosts()
            ).subscribe({ response ->
                val mRefinedLatLngDto: MutableList<LatLngDto> = mutableListOf()
                for(model in response){
                    if(model.latlng.size == 2){
                        mRefinedLatLngDto.add(model.convertToLatLngDto()
                        )
                    }
                }
                getView()?.showAllCountryMarkersOnMap(mRefinedLatLngDto)
            }, { throwable ->
                throwable.printStackTrace()
                getView()?.hideProgress()
            })
        )
    }
}