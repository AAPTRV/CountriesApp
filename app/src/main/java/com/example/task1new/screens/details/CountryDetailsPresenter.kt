package com.example.task1new.screens.details

import com.example.task1new.OkRetrofit
import com.example.task1new.base.mvp.BaseMvpPresenter
import com.google.android.gms.maps.model.LatLng

class CountryDetailsPresenter : BaseMvpPresenter<CountryDetailsView>() {

    fun getCountryByName(name: String, isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(OkRetrofit.jsonPlaceHolderApi.getCountryByName(name), false)
            ).subscribe(
                {
                    getView()?.showCountryInfo(
                        it[0].convertToPostCountryItemDto(), LatLng(
                            it[0].convertToLatLngDto().mLatitude,
                            it[0].convertToLatLngDto().mLongitude
                        )
                    )
                }, { throwable ->
                    throwable.message?.let { errorMessage -> getView()?.showError(errorMessage, throwable) }
                }
            )
        )
    }
}