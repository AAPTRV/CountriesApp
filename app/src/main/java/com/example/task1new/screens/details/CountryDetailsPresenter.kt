package com.example.task1new.screens.details

import com.example.task1new.network.Retrofit
import com.example.task1new.base.mvp.BaseMvpPresenter
import com.google.android.gms.maps.model.LatLng

class CountryDetailsPresenter : BaseMvpPresenter<CountryDetailsView>() {

    fun getCountryByName(name: String, isRefresh: Boolean) {
        if (!isRefresh) {
            getView()?.showProgress()
        }
        addDisposable(
            inBackground(
                Retrofit.COUNTRY_SERVICE.getCountryByName(name)
            ).subscribe(
                {
                    getView()?.showCountryInfo(
                        it[0].convertToCountryItemDto(), LatLng(
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
                        if (!isRefresh) {
                            getView()?.hideProgress()
                        }
                    }
                }
            )
        )
    }
}