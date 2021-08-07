package com.example.task1new.screens.countryList

import android.content.ContentValues.TAG
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.task1new.Retrofit
import com.example.task1new.app.CountriesApp
import com.example.task1new.base.filter.CountryDtoListFilterObject
import com.example.task1new.base.mvvm.BaseViewModel
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.base.mvvm.executeJob
import com.example.task1new.dto.CountryDto
import com.example.task1new.ext.convertCommonInfoAPIDatatoDBItem
import com.example.task1new.ext.convertLanguagesAPIDataToDBItem
import com.example.task1new.model.convertToCountryDto
import com.example.task1new.room.*
import com.example.task1new.transformer.DaoEntityToDtoTransformer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.math.max
import kotlin.math.min

class CountryListViewModel(
    savedStateHandle: SavedStateHandle,
    mDataBase: DBInfo = CountriesApp.mDatabase
) :
    BaseViewModel(savedStateHandle) {

    private var mUsersLocation: Location? = null

    private var mDaoCountryInfo: CountryCommonInfoDAO = mDataBase.getCountryCommonInfoDAO()
    private var mDaoLanguageInfo: CountryLanguageDAO = mDataBase.getLanguageCommonInfoDAO()
    private val mCountriesListLiveData =
        savedStateHandle.getLiveData<List<CountryDto>>("countryListDto")

    private val mCountriesFilterLiveData =
        savedStateHandle.getLiveData<CountryDtoListFilterObject>("countryListDtoFilter")

    private val mFilter = CountryDtoListFilterObject

    fun attachCurrentLocation(location: Location) {
        mUsersLocation = location
    }

    fun addDistanceToCountriesLiveData() {
        val result: MutableList<CountryDto> = mutableListOf()
        mCountriesListLiveData.value?.let { result.addAll(it) }
        for (dto in result) {
            if (dto.location.isNotEmpty()) {
                val currentCountryLocation = Location(LocationManager.GPS_PROVIDER).apply {
                    latitude = dto.location[0]
                    longitude = dto.location[1]
                }
                dto.distance =
                    (mUsersLocation!!.distanceTo(currentCountryLocation)
                        .toDouble() / 1000).toString()
            }
        }
        mCountriesListLiveData.value = result
    }

    private fun calculateDistanceToUser(dto: CountryDto): Double {
        var result = 0.0
        if (dto.location.isNotEmpty()) {
            val currentCountryLocation = Location(LocationManager.GPS_PROVIDER).apply {
                latitude = dto.location[0]
                longitude = dto.location[1]
            }

            mUsersLocation?.let {
                result =
                    (mUsersLocation!!.distanceTo(currentCountryLocation).toDouble() / 1000)
            }
        }
        return result
    }

    fun getMaximumDistance(): String {
        var mDistanceMax: Double = Double.MIN_VALUE
        mCountriesListLiveData.value.let {
            for (country in mCountriesListLiveData.value!!) {
                if (calculateDistanceToUser(country) > mDistanceMax) {
                    mDistanceMax = calculateDistanceToUser(country)
                }
            }
        }
        return mDistanceMax.toString()
    }

    fun getMinimumDistance(): String {
        var mDistanceMin: Double = Double.MAX_VALUE
        mCountriesListLiveData.value.let {
            for (country in mCountriesListLiveData.value!!) {
                if (country.location.isNotEmpty()) {
                    if (calculateDistanceToUser(country).toDouble() < mDistanceMin) {
                        mDistanceMin = calculateDistanceToUser(country).toDouble()
                    }
                }
            }
        }

        return mDistanceMin.toString()
    }

    fun getMinimumArea(): String {
        var mAreaMin: Double = Double.MAX_VALUE
        mCountriesListLiveData.value.let {
            for (country in mCountriesListLiveData.value!!) {
                if (country.area < mAreaMin) {
                    mAreaMin = country.area
                }
            }
        }
        return mAreaMin.toString()
    }

    fun getMaximumArea(): String {
        var mAreaMax: Double = Double.MIN_VALUE
        mCountriesListLiveData.value.let {
            for (country in mCountriesListLiveData.value!!) {
                if (country.area > mAreaMax) {
                    mAreaMax = country.area
                }
            }
        }
        return mAreaMax.toString()
    }

    fun getMinimumPopulation(): String {
        var mPopulationMin: Int = Int.MAX_VALUE
        mCountriesListLiveData.value.let {
            for (country in mCountriesListLiveData.value!!) {
                if (country.population < mPopulationMin) {
                    mPopulationMin = country.population
                }
            }
        }
        return mPopulationMin.toString()
    }

    fun getMaximumPopulation(): String {
        var mPopulationMax: Int = Int.MIN_VALUE
        mCountriesListLiveData.value.let {
            for (country in mCountriesListLiveData.value!!) {
                if (country.population > mPopulationMax) {
                    mPopulationMax = country.population
                }
            }
        }
        return mPopulationMax.toString()
    }

    fun getFilterLiveData(): MutableLiveData<CountryDtoListFilterObject> {
        return mCountriesFilterLiveData
    }

    private fun getFilter(): CountryDtoListFilterObject = mFilter

    fun clearFilterExceptName() {
        getFilter().filterClearExceptName()
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterCountryName(countryName: String) {
        getFilter().filterCountryNameChange(countryName)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMinPopulation(minPopulation: Int) {
        getFilter().filterMinPopulationChange(minPopulation)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMaxPopulation(maxPopulation: Int) {
        getFilter().filterMaxPopulationChange(maxPopulation)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMinArea(minArea: Double) {
        getFilter().filterMinAreaChange(minArea)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMaxArea(maxArea: Double) {
        getFilter().filterMaxAreaChange(maxArea)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMaxDistance(maxDistance: Double) {
        getFilter().filterMaxDistanceChange(maxDistance)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun getCountriesListLiveData(): MutableLiveData<List<CountryDto>> {
        return mCountriesListLiveData
    }

    fun getCountriesFromDb() {
        val mCountriesLanguageEntities = mutableListOf<CountryDatabaseLanguageInfoEntity>()
        val mCountriesInfoEntities = mutableListOf<CountryDatabaseCommonInfoEntity>()
        val mPostCountriesData = mutableListOf<CountryDto>()
        // Filling mCountriesInfoEntities list with items from DB
        mDaoCountryInfo.getAllInfo().forEach { entity ->
            mCountriesInfoEntities.add(entity)
        }
        // Filling mCountriesLanguageEntities with items from DB
        mCountriesInfoEntities.forEach { entity ->
            mCountriesLanguageEntities.add(mDaoLanguageInfo.getLanguageInfoByCountry(entity.name))
        }
        // Filling mPost Countries Data through transformer, using info and languages entities
        mCountriesInfoEntities.forEachIndexed { index, infoEntity ->
            mPostCountriesData.add(
                DaoEntityToDtoTransformer.daoEntityToDtoTransformer(
                    infoEntity,
                    mCountriesLanguageEntities[index]
                )
            )
        }
        if (mPostCountriesData.isNotEmpty()) {
            // Filling adapter with first 20 items from DB
            mCountriesListLiveData.value = mPostCountriesData
        }
        Log.e(
            TAG,
            "GOT COUNTRIES FROM DB. SIZE = ${mPostCountriesData.size}.  LIVE DATA SIZE = ${mCountriesListLiveData.value?.size}"
        )
    }

    fun getCountriesFromAPI() {
        mCompositeDisposable.add(
            Retrofit.getCountriesApi().getPosts()
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
                    Log.e(TAG, "GET COUNTRIES FROM API TO DB")
                }
                .map { it.convertToCountryDto() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ countriesDtoList ->
                    addDistanceToCountriesLiveData()
                    Log.e(
                        TAG,
                        "GOT COUNTRIES FROM API. SIZE = ${countriesDtoList.size}. LIVE DATA SIZE = ${mCountriesListLiveData.value?.size}"
                    )

                }, { getCountriesFromDb() }
                )
        )
    }
}