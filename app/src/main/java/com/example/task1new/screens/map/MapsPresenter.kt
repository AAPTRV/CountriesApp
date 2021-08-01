package com.example.task1new.screens.map

import com.example.task1new.Retrofit
import com.example.task1new.base.mvp.BaseMvpPresenter
import com.example.task1new.dto.LatLngDto

class MapsPresenter : BaseMvpPresenter<MapsView>() {
    fun getDataFromRetrofitToShowMarkers() {
        getView()?.showProgress()
        addDisposable(
            inBackground(
                Retrofit.jsonPlaceHolderApi.getPosts()
            ).subscribe({ response ->
                val mRefinedLatLngDto: MutableList<LatLngDto> = mutableListOf()
                for(model in response){
                    if(model.latlng.isNotEmpty()){
                        mRefinedLatLngDto.add(model.convertToLatLngDto()
                        )
                    }
                }
                getView()?.showAllCountryMarkersOnMap(mRefinedLatLngDto)
                getView()?.hideProgress()
            }, { throwable ->
                throwable.printStackTrace()
                getView()?.hideProgress()
            })
        )
    }
}