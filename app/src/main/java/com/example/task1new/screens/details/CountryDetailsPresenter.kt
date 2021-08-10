package com.example.task1new.screens.details

import com.example.data.network.Retrofit
import com.example.domain.repository.NetworkRepository
import com.example.domain.usecase.impl.GetCountryListByNameUseCase
import com.example.task1new.base.mvp.BaseMvpPresenter
import com.google.android.gms.maps.model.LatLng
import com.repository.networkRepo.NetworkRepositoryImpl

class CountryDetailsPresenter : BaseMvpPresenter<CountryDetailsView>() {

    private val mNetworkRepository: NetworkRepository = NetworkRepositoryImpl(Retrofit.getCountriesApi())
    private val mGetCountryListByNameUseCase: GetCountryListByNameUseCase = GetCountryListByNameUseCase(mNetworkRepository)

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