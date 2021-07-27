package com.example.task1new.screens.map

import android.content.ContentValues
import android.util.Log
import com.example.task1new.OkRetrofit
import com.example.task1new.base.mvp.BaseMvpPresenter

class MapsPresenter: BaseMvpPresenter<MapsView>() {
    fun getDataFromRetrofitToShowMarkers(){
        addDisposable(
            inBackground(
                handleProgress(OkRetrofit.jsonPlaceHolderApi.getPosts(), false)
            ).subscribe({ response ->
                Log.e(ContentValues.TAG, "getDataStart")
                getView()?.showAllCountryMarkersOnMap(response)
            }, { throwable -> throwable.printStackTrace() })
        )
    }
}