package com.example.task1new.screens.details

import com.example.task1new.OkRetrofit
import com.example.task1new.base.mvp.BaseMvpPresenter
import com.example.task1new.dto.PostCountryItemDto
import com.google.android.gms.maps.model.LatLng
import io.reactivex.rxjava3.core.Flowable

class CountryDetailsPresenter : BaseMvpPresenter<CountryDetailsView>() {

    fun getCountryByName(name: String, isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(OkRetrofit.jsonPlaceHolderApi.getCountryByName(name), isRefresh)
            ).subscribe({
                getView()?.showCountryInfo(
                    it[0].convertToPostCountryItemDto(), LatLng(
                        it[0].convertToLatLngDto().mLatitude,
                        it[0].convertToLatLngDto().mLongitude
                    )
                )
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )

    }

}
