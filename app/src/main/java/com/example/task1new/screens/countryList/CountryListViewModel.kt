package com.example.task1new.screens.countryList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.task1new.Retrofit
import com.example.task1new.base.mvvm.BaseViewModel
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.base.mvvm.executeJob
import com.example.task1new.dto.CountryDto
import com.example.task1new.model.convertToCountryDto

class CountryListViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel(savedStateHandle) {

    private val mCountryItemLiveData =
        savedStateHandle.getLiveData<Outcome<CountryDto>>("countryDto")
    private val mCountriesListLiveData =
        savedStateHandle.getLiveData<Outcome<List<CountryDto>>>("countryListDto")
    private val mCountriesFilteredListLiveData =
        savedStateHandle.getLiveData<Outcome<List<CountryDto>>>("countryFilteredListDto")

    fun getCountryLiveData(): MutableLiveData<Outcome<CountryDto>> {
        return mCountryItemLiveData
    }

    fun getCountriesListLiveData(): MutableLiveData<Outcome<List<CountryDto>>>{
        return mCountriesListLiveData
    }

    fun getCountryByName() {
        mCompositeDisposable.add(
            executeJob(
                Retrofit.jsonPlaceHolderApi.getCountryByName(
                    "belarus"
                )
                    .map { it[0].convertToCountryItemDto() }, mCountryItemLiveData
            )
        )
    }

    fun getCountries() {
        mCompositeDisposable.add(
            executeJob(
                Retrofit.jsonPlaceHolderApi.getPosts()
                    .map { it.convertToCountryDto() }, mCountriesListLiveData
            )
        )
    }

//    fun getDataFromRetrofitToRecycleAdapter(isRefresh: Boolean) {
//        if (!isRefresh) {
//            getView()?.showProgress()
//        }
//        addDisposable(
//            inBackground(
//                Retrofit.jsonPlaceHolderApi.getPosts()
//                    .doOnNext {
//                        // DB inserting data
//                        val mCountriesInfoFromAPI = it.toMutableList()
//                        val mCountriesInfoToDB = mutableListOf<CountryDatabaseCommonInfoEntity>()
//
//                        val mLanguagesFromApiToDB =
//                            mutableListOf<CountryDatabaseLanguageInfoEntity>()
//                        mCountriesInfoFromAPI.slice(1..20).forEach { item ->
//                            mLanguagesFromApiToDB.add(item.convertLanguagesAPIDataToDBItem())
//                        }
//                        mDaoLanguageInfo.addAll(mLanguagesFromApiToDB)
//
//                        mCountriesInfoFromAPI.slice(1..20).forEach { item ->
//                            mCountriesInfoToDB.add(
//                                item.convertCommonInfoAPIDatatoDBItem()
//                            )
//                            mDaoCountryInfo.addAll(mCountriesInfoToDB)
//                        }
//                    }
//                    .map { it.convertToCountryDto() }
//            ).subscribe({ dto ->
//                getView()?.addNewUniqueItemsInRecycleAdapter(dto)
//                if (!isRefresh) {
//                    getView()?.hideProgress()
//                }
//
//            }, { throwable ->
//                throwable.printStackTrace()
//                if (!isRefresh) {
//                    getView()?.hideProgress()
//                }
//            })
//        )
//    }

//    fun getCountryByName(name: String, isRefresh: Boolean) {
//        if (!isRefresh) {
//            getView()?.showProgress()
//        }
//        addDisposable(
//            inBackground(
//                Retrofit.jsonPlaceHolderApi.getCountryByName(name)
//            ).subscribe(
//                {
//                    getView()?.showCountryInfo(
//                        it[0].convertToCountryItemDto(), LatLng(
//                            it[0].convertToLatLngDto().mLatitude,
//                            it[0].convertToLatLngDto().mLongitude
//                        )
//                    )
//                    if (!isRefresh) {
//                        getView()?.hideProgress()
//                    }
//                }, { throwable ->
//                    throwable.message?.let { errorMessage ->
//                        getView()?.showError(errorMessage, throwable)
//                        if (!isRefresh) {
//                            getView()?.hideProgress()
//                        }
//                    }
//                }
//            )
//        )
//    }
}