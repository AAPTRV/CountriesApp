package com.example.task1new.screens.details

import android.nfc.Tag
import android.util.Log
import com.example.task1new.OkRetrofit
import com.example.task1new.base.mvp.BaseMvpPresenter
import com.google.android.gms.maps.model.LatLng
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class CountryDetailsPresenter : BaseMvpPresenter<CountryDetailsView>() {

    fun getCountryByName(name: String, isRefresh: Boolean) {
//        if (!isRefresh) {
//            getView()?.showProgress()
//        }
        addDisposable(
            inBackground(
                handleProgress(OkRetrofit.jsonPlaceHolderApi.getCountryByName(name), isRefresh)
            ).subscribe(
                {
                    getView()?.showCountryInfo(
                        it[0].convertToPostCountryItemDto(), LatLng(
                            it[0].convertToLatLngDto().mLatitude,
                            it[0].convertToLatLngDto().mLongitude
                        )
                    )
                    if (!isRefresh) {
                        getView()?.hideProgress()
                    }
                }, { throwable ->
                    throwable.message?.let { errorMessage ->
                        getView()?.showError(errorMessage, throwable)
                        println("ERROR -> $errorMessage")
//                        if (!isRefresh) {
//                            getView()?.hideProgress()
//                        }
                    }
                }
            )
        )
    }
}