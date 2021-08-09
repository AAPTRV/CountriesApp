package com.example.task1new.screens.countryList

import android.location.Location
import android.location.LocationManager
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
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Flow

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

    private val mCountriesListLiveDataRx =
        savedStateHandle.getLiveData<Outcome<List<CountryDto>>>("countryListDto")

    fun getCountriesLiveDataRx(): MutableLiveData<Outcome<List<CountryDto>>> {
        return mCountriesListLiveDataRx
    }


    fun attachCurrentLocation(location: Location) {
        mUsersLocation = location
    }

    private fun addDistanceToCountriesLiveData() {
        val result: MutableList<CountryDto> = mutableListOf()
        mCountriesListLiveData.value?.let { result.addAll(it) }
        for (dto in result) {
            if (dto.location.isNotEmpty()) {
                val currentCountryLocation = Location(LocationManager.GPS_PROVIDER).apply {
                    latitude = dto.location[0]
                    longitude = dto.location[1]
                }
                mUsersLocation?.let {
                    dto.distance =
                        (mUsersLocation!!.distanceTo(currentCountryLocation)
                            .toDouble() / 1000).toString()
                }
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
                if (country.location.isNotEmpty()) {
                    if (calculateDistanceToUser(country) > mDistanceMax) {
                        mDistanceMax = calculateDistanceToUser(country)
                    }
                }
            }
        }
        return mDistanceMax.toString()
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

    fun getMinimumPopulation(): Int {
        var mPopulationMin: Int = Int.MAX_VALUE
        mCountriesListLiveData.value.let {
            for (country in mCountriesListLiveData.value!!) {
                if (country.population < mPopulationMin) {
                    mPopulationMin = country.population
                }
            }
        }
        return mPopulationMin
    }

    fun getMaximumPopulation(): Int {
        var mPopulationMax: Int = Int.MIN_VALUE
        mCountriesListLiveData.value.let {
            for (country in mCountriesListLiveData.value!!) {
                if (country.population > mPopulationMax) {
                    mPopulationMax = country.population
                }
            }
        }
        return mPopulationMax
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

    fun getCountriesFromDbRx() {
        mCompositeDisposable.add(
            Flowable.fromIterable(mDaoCountryInfo.getAllInfo())
                .map { entity ->
                    mDaoLanguageInfo.getLanguageInfoByCountry(entity.name)
                }
                .collect(
                    { mutableListOf<CountryDatabaseLanguageInfoEntity>() },
                    { entityList, entity ->
                        entityList.add(entity)
                    })
                .map { languageEntitiesList -> }
                .subscribe({ it ->

                }, {})
        )
    }

    fun getCountriesFromDbRxSimple() {
        val mCountriesLanguageEntities = mutableListOf<CountryDatabaseLanguageInfoEntity>()
        val mCountriesInfoEntities = mutableListOf<CountryDatabaseCommonInfoEntity>()
        val mPostCountriesData = mutableListOf<CountryDto>()
        // Filling mCountriesInfoEntities list with items from DB +
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
        mCompositeDisposable.add(
            executeJob(
                Flowable.just(mPostCountriesData),
                mCountriesListLiveDataRx
            )
        )
    }

    fun getCountriesFromAPIRx() {
        executeJob(
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
                }
                .map { it.convertToCountryDto() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()), mCountriesListLiveDataRx
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
                }
                .map { it.convertToCountryDto() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mCountriesListLiveData.value
                    addDistanceToCountriesLiveData()
                }, {}
                )
        )
    }
}


//fun getCountriesFromDb() {
//    val mCountriesLanguageEntities = mutableListOf<CountryDatabaseLanguageInfoEntity>()
//    val mCountriesInfoEntities = mutableListOf<CountryDatabaseCommonInfoEntity>()
//    val mPostCountriesData = mutableListOf<CountryDto>()
//    // Filling mCountriesInfoEntities list with items from DB +
//    mDaoCountryInfo.getAllInfo().forEach { entity ->
//        mCountriesInfoEntities.add(entity)
//    }
//    // Filling mCountriesLanguageEntities with items from DB
//    mCountriesInfoEntities.forEach { entity ->
//        mCountriesLanguageEntities.add(mDaoLanguageInfo.getLanguageInfoByCountry(entity.name))
//    }
//    // Filling mPost Countries Data through transformer, using info and languages entities
//    mCountriesInfoEntities.forEachIndexed { index, infoEntity ->
//        mPostCountriesData.add(
//            DaoEntityToDtoTransformer.daoEntityToDtoTransformer(
//                infoEntity,
//                mCountriesLanguageEntities[index]
//            )
//        )
//    }
//    if (mPostCountriesData.isNotEmpty()) {
//        // Filling adapter with first 20 items from DB
//        mCountriesListLiveData.value = mPostCountriesData
//    }
//}