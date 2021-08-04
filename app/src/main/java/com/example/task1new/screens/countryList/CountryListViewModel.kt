package com.example.task1new.screens.countryList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.task1new.Retrofit
import com.example.task1new.base.mvvm.BaseViewModel
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.base.mvvm.executeJob
import com.example.task1new.dto.CountryDto
import com.example.task1new.ext.convertCommonInfoAPIDatatoDBItem
import com.example.task1new.ext.convertLanguagesAPIDataToDBItem
import com.example.task1new.model.convertToCountryDto
import com.example.task1new.room.*
import com.example.task1new.transformer.DaoEntityToDtoTransformer

class CountryListViewModel(savedStateHandle: SavedStateHandle, mDataBase: DBInfo) : BaseViewModel(savedStateHandle) {

    private var mDaoCountryInfo: CountryCommonInfoDAO = mDataBase.getCountryCommonInfoDAO()
    private var mDaoLanguageInfo: CountryLanguageDAO = mDataBase.getLanguageCommonInfoDAO()

    private val mCountryItemLiveData =
        savedStateHandle.getLiveData<Outcome<CountryDto>>("countryDto")
    private val mCountriesListLiveData =
        savedStateHandle.getLiveData<Outcome<List<CountryDto>>>("countryListDto")
    private val mCountriesFilteredListLiveData =
        savedStateHandle.getLiveData<Outcome<List<CountryDto>>>("countryFilteredListDto")

    fun getCountryLiveData(): MutableLiveData<Outcome<CountryDto>> {
        return mCountryItemLiveData
    }

    fun getCountriesListLiveData(): MutableLiveData<Outcome<List<CountryDto>>> {
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
                    .doOnNext {
                        // DB inserting data
                        val mCountriesInfoFromAPI = it.toMutableList()
                        val mCountriesInfoToDB = mutableListOf<CountryDatabaseCommonInfoEntity>()

                        val mLanguagesFromApiToDB =
                            mutableListOf<CountryDatabaseLanguageInfoEntity>()
                        mCountriesInfoFromAPI.slice(1..20).forEach { item ->
                            mLanguagesFromApiToDB.add(item.convertLanguagesAPIDataToDBItem())
                        }
                        mDaoLanguageInfo.addAll(mLanguagesFromApiToDB)

                        mCountriesInfoFromAPI.slice(1..20).forEach { item ->
                            mCountriesInfoToDB.add(
                                item.convertCommonInfoAPIDatatoDBItem()
                            )
                            mDaoCountryInfo.addAll(mCountriesInfoToDB)
                        }
                    }
                    .map { it.convertToCountryDto() }, mCountriesListLiveData
            )
        )
    }
}