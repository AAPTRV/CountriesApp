package com.example.task1new.screens.countryList

import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.task1new.Retrofit
import com.example.task1new.app.CountriesApp
import com.example.task1new.utils.filter.CountryDtoListFilterObject
import com.example.task1new.utils.filter.CountryDtoListFilterObject.applyFilter
import com.example.task1new.base.mvvm.BaseViewModel
import com.example.task1new.dto.CountryDto
import com.example.task1new.ext.convertCommonInfoAPIDatatoDBItem
import com.example.task1new.ext.convertLanguagesAPIDataToDBItem
import com.example.task1new.model.convertToCountryDto
import com.example.task1new.repository.FilterRepository
import com.example.task1new.repository.FilterRepositoryImpl
import com.example.task1new.room.*
import com.example.task1new.transformer.DaoEntityToDtoTransformer
import com.example.task1new.transformer.DaoEntityToDtoTransformer.Companion.findEntityByCountryName
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.BiFunction

class CountryListViewModel(
    savedStateHandle: SavedStateHandle,
    mDataBase: DBInfo = CountriesApp.mDatabase,
) :
    BaseViewModel(savedStateHandle) {

    private var mUsersLocation: Location? = null
    private val mFilter: FilterRepository = FilterRepositoryImpl()
    private var mDaoCountryInfo: CountryCommonInfoDAO = mDataBase.getCountryCommonInfoDAO()
    private var mDaoLanguageInfo: CountryLanguageDAO = mDataBase.getLanguageCommonInfoDAO()
    private val mCountriesListLiveData =
        savedStateHandle.getLiveData<List<CountryDto>>("countryListDto")

    private val mCountriesFilterLiveData =
        savedStateHandle.getLiveData<CountryDtoListFilterObject>("countryListDtoFilter")

    fun attachCurrentLocation(location: Location) {
        mUsersLocation = location
    }

    fun getFilteredDataFromCountriesLiveData(): List<CountryDto> {
        val result: MutableList<CountryDto> = mutableListOf()
        mCountriesListLiveData.value?.let { result.addAll(it) }
        return result.applyFilter(mFilter.getFilter())
    }

    private fun addDistanceToCountriesDtoList(dtoList: List<CountryDto>): List<CountryDto> {
        val result: MutableList<CountryDto> = mutableListOf()
        result.addAll(dtoList)
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
        Log.e("HZ", "Data size after appliing distance: ${result.size}")
        return result
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

//    private fun getFilter(): CountryDtoListFilterObject = mFilter

    fun clearFilterExceptName() {
        mFilter.getFilter().filterClearExceptName()
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterCountryName(countryName: String) {
        mFilter.getFilter().filterCountryNameChange(countryName)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMinPopulation(minPopulation: Int) {
        mFilter.getFilter().filterMinPopulationChange(minPopulation)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMaxPopulation(maxPopulation: Int) {
        mFilter.getFilter().filterMaxPopulationChange(maxPopulation)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMinArea(minArea: Double) {
        mFilter.getFilter().filterMinAreaChange(minArea)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMaxArea(maxArea: Double) {
        mFilter.getFilter().filterMaxAreaChange(maxArea)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMaxDistance(maxDistance: Double) {
        mFilter.getFilter().filterMaxDistanceChange(maxDistance)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun getCountriesListLiveData(): MutableLiveData<List<CountryDto>> {
        return mCountriesListLiveData
    }

    private fun addNewUniqueItemsInLiveData(dtoList: List<CountryDto>) {
        if (mCountriesListLiveData.value.isNullOrEmpty()) {
            mCountriesListLiveData.value = dtoList
        } else {
            val result = mutableListOf<CountryDto>()
            val initialDtoList = mutableListOf<CountryDto>()
            initialDtoList.addAll(mCountriesListLiveData.value!!)
            for (newDto in dtoList) {
                var isUnique = true
                for (dto in initialDtoList) {
                    if (newDto.name == dto.name) {
                        if (newDto.distance == dto.distance) {
                            isUnique = false
                        }
                    }
                }
                if (isUnique) {
                    result.add(newDto)
                }
            }
            mCountriesListLiveData.value = result
        }
    }

    fun getCountriesFromDbRx() {

        fun getDtoFromEntities(
            infoEntities: List<CountryDatabaseCommonInfoEntity>,
            languageEntities: List<CountryDatabaseLanguageInfoEntity>
        ): List<CountryDto> {

            val result = mutableListOf<CountryDto>()
            for (entity in infoEntities) {
                result.add(
                    DaoEntityToDtoTransformer.daoEntityToDtoTransformer(
                        entity,
                        languageEntities.findEntityByCountryName(entity.name)
                    )
                )
            }
            return result
        }

        fun getCountriesInfoEntitiesFlowable(): Flowable<List<CountryDatabaseCommonInfoEntity>> {
            return Flowable.just("")
                .map { mDaoCountryInfo.getAllInfo() }
                .doOnNext { Log.e("HZ", "Common entities Flowable thread is: ${Thread.currentThread().name}") }
        }

        fun getCountriesLanguageEntitiesFlowable(): Flowable<List<CountryDatabaseLanguageInfoEntity>>{
            return Flowable.just("")
                .map{mDaoLanguageInfo.getAllInfo()}
                .doOnNext { Log.e("HZ", "Common language Flowable thread is: ${Thread.currentThread().name}") }

        }

        mCompositeDisposable.add(
            Flowable.zip(
                getCountriesInfoEntitiesFlowable(),
                getCountriesLanguageEntitiesFlowable(),
                io.reactivex.rxjava3.functions.BiFunction{ commonInfoEntityList, languageInfoEntityList ->
                    return@BiFunction getDtoFromEntities(
                        commonInfoEntityList,
                        languageInfoEntityList
                    )
                })
                // TODO: Adding distance is not working!
                .map { addDistanceToCountriesDtoList(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { addNewUniqueItemsInLiveData(it) })
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
        addNewUniqueItemsInLiveData(mPostCountriesData)
        Log.e("hz", "GET DB LIVE DATA SIZE = ${mCountriesListLiveData.value?.size}")
    }
}

fun getCountriesFromAPI() {
    mCompositeDisposable.add(
        Retrofit.getCountriesApi().getPosts()
            .doOnNext {
                Log.e("hz", "DO ON NEXT")
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
//                    addDistanceToCountriesLiveData()
            }
            .map { it.convertToCountryDto() }
            .map { addDistanceToCountriesDtoList(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                addNewUniqueItemsInLiveData(it)
                Log.e("hz", "GET API LIVE DATA SIZE = ${mCountriesListLiveData.value?.size}")
            }, { }
            )
    )
}
}