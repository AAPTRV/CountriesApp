package com.example.task1new.screens.countryList

import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.data.repository.databaseRepo.DatabaseCommonInfoRepositoryImpl
import com.example.task1new.base.mvvm.*
import com.example.domain.filter.CountryDtoListFilterObject
import com.example.domain.filter.CountryDtoListFilterObject.applyFilter
import com.example.domain.dto.CountryDto
import com.example.domain.repository.network.NetworkRepository
import com.example.task1new.ext.convertToCommonInfoDbItem
import com.example.task1new.ext.convertToLanguagesDbItem
import com.example.data.repository.filterRepo.FilterRepositoryImpl
import com.example.data.room.convertToEntity
import com.example.data.room.convertToRoomDto
import com.example.domain.repository.database.DatabaseCommonInfoRepository
import com.example.domain.repository.database.DatabaseLanguageRepository
import com.example.data.transformer.DaoEntityToDtoTransformer
import com.example.data.transformer.DaoEntityToDtoTransformer.Companion.findEntityByCountryName
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

class CountryListViewModel(
    savedStateHandle: SavedStateHandle,
    // TODO Create DB repository
    mDataBase: com.example.data.room.DBInfo,
    private val mNetworkRepository: NetworkRepository,
    private val mDataBaseCommonInfoRepository: DatabaseCommonInfoRepository,
    private val mDataBaseLanguageRepository: DatabaseLanguageRepository
) :
    BaseViewModel(savedStateHandle) {

    private var mFilter = FilterRepositoryImpl().getFilter()
    private var mUsersLocation: Location? = null
//    private var mDaoCountryInfo = DatabaseCommonInfoRepositoryImpl(DB)
//    private var mDaoLanguageInfo: com.example.data.room.CountryLanguageDAO = mDataBaseCommonInfoRepository.getLanguageCommonInfoDAO()

    private val mCountriesListLiveData =
        savedStateHandle.getLiveData<Outcome<List<CountryDto>>>("countryListDto")

    private val mCountriesFilterLiveData =
        savedStateHandle.getLiveData<CountryDtoListFilterObject>("countryListDtoFilter")


    fun attachCurrentLocation(location: Location) {
        mUsersLocation = location
    }

    fun getFilteredDataFromCountriesLiveData(): List<CountryDto> {
        val result: MutableList<CountryDto> = mutableListOf()
        if (mCountriesListLiveData.value is Outcome.Success) {
            result.addAll((mCountriesListLiveData.value as Outcome.Success).data)
        }
        return result.applyFilter(mFilter)
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
                    Log.e("HZ", "We are in mUsersLocation != null")
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
                val userLocation = it
                result =
                    (userLocation.distanceTo(currentCountryLocation).toDouble() / 1000)
            }
        }
        return result
    }

    fun getMaximumDistance(): String {
        var mDistanceMax: Double = Double.MIN_VALUE
        if (mCountriesListLiveData.value is Outcome.Success) {
            for (country in (mCountriesListLiveData.value as Outcome.Success).data) {
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
        if (mCountriesListLiveData.value is Outcome.Success) {
            for (country in (mCountriesListLiveData.value as Outcome.Success).data) {
                if (country.area < mAreaMin) {
                    mAreaMin = country.area
                }
            }
        }
        return mAreaMin.toString()
    }

    fun getMaximumArea(): String {
        var mAreaMax: Double = Double.MIN_VALUE
        if (mCountriesListLiveData.value is Outcome.Success) {
            for (country in (mCountriesListLiveData.value as Outcome.Success).data) {
                if (country.area > mAreaMax) {
                    mAreaMax = country.area
                }
            }
        }
        return mAreaMax.toString()
    }

    fun getMinimumPopulation(): String {
        var mPopulationMin: Int = Int.MAX_VALUE
        if (mCountriesListLiveData.value is Outcome.Success) {
            for (country in (mCountriesListLiveData.value as Outcome.Success).data) {
                if (country.population < mPopulationMin) {
                    mPopulationMin = country.population
                }
            }
        }
        return mPopulationMin.toString()
    }

    fun getMaximumPopulation(): String {
        var mPopulationMax: Int = Int.MIN_VALUE
        if (mCountriesListLiveData.value is Outcome.Success) {
            for (country in (mCountriesListLiveData.value as Outcome.Success).data) {
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

    fun clearFilterExceptName() {
        mFilter.filterClearExceptName()
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterCountryName(countryName: String) {
        mFilter.filterCountryNameChange(countryName)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMinPopulation(minPopulation: Int) {
        mFilter.filterMinPopulationChange(minPopulation)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMaxPopulation(maxPopulation: Int) {
        mFilter.filterMaxPopulationChange(maxPopulation)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMinArea(minArea: Double) {
        mFilter.filterMinAreaChange(minArea)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMaxArea(maxArea: Double) {
        mFilter.filterMaxAreaChange(maxArea)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun setFilterMaxDistance(maxDistance: Double) {
        mFilter.filterMaxDistanceChange(maxDistance)
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun getCountriesListLiveData(): MutableLiveData<Outcome<List<CountryDto>>> {
        return mCountriesListLiveData
    }

    private fun addNewUniqueItemsInLiveData(dtoList: List<CountryDto>) {

        if (mCountriesListLiveData.value is Outcome.Next) {

            if ((mCountriesListLiveData.value as Outcome.Next).data.isNullOrEmpty()) {
                (mCountriesListLiveData.value as Outcome.Next).data = dtoList
            } else {
                val result = mutableListOf<CountryDto>()
                val initialDtoList = mutableListOf<CountryDto>()
                initialDtoList.addAll((mCountriesListLiveData.value as Outcome.Next).data)
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
                (mCountriesListLiveData.value as Outcome.Next).data = result
            }
        }
    }

    fun getCountriesFromDbRx() {

        fun getDtoFromEntities(
            infoEntities: List<com.example.data.room.CountryDatabaseCommonInfoEntity>,
            languageEntities: List<com.example.data.room.CountryDatabaseLanguageInfoEntity>
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

        fun getCountriesInfoEntitiesFlowable(): Flowable<List<com.example.data.room.CountryDatabaseCommonInfoEntity>> {
            return Flowable.create({
                val result = mDataBaseCommonInfoRepository.getAllInfo()
                it.onNext(result.convertToEntity())
                it.onComplete()
            }, BackpressureStrategy.LATEST)
        }

        fun getCountriesLanguageEntitiesFlowable(): Flowable<List<com.example.data.room.CountryDatabaseLanguageInfoEntity>> {
            return Flowable.create({
                val result = mDataBaseLanguageRepository.getAllInfo()
                it.onNext(result.convertToEntity())
                it.onComplete()
            }, BackpressureStrategy.LATEST)
        }
        mCountriesListLiveData.loading(true)
        mCompositeDisposable.add(
            Flowable.zip(
                getCountriesInfoEntitiesFlowable(),
                getCountriesLanguageEntitiesFlowable(),
                io.reactivex.rxjava3.functions.BiFunction { commonInfoEntityList, languageInfoEntityList ->
                    return@BiFunction getDtoFromEntities(
                        commonInfoEntityList,
                        languageInfoEntityList
                    )
                })
                .map { addDistanceToCountriesDtoList(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mCountriesListLiveData.success(it)})
    }

    fun getCountriesFromAPI() {
        mCountriesListLiveData.loading(true)
        mCompositeDisposable.add(
            mNetworkRepository.getCountryList()
                .doOnNext {
                    // DB inserting data
                    val mCountriesInfoFromAPI = it.toMutableList()
                    val mCountriesInfoToDB = mutableListOf<com.example.data.room.CountryDatabaseCommonInfoEntity>()

                    val mLanguagesFromApiToDB =
                        mutableListOf<com.example.data.room.CountryDatabaseLanguageInfoEntity>()
                    mCountriesInfoFromAPI.slice(1..20).forEach { item ->
                        mLanguagesFromApiToDB.addAll(item.convertToLanguagesDbItem())
                    }
                    mDataBaseLanguageRepository.addAll(mLanguagesFromApiToDB.convertToRoomDto())

                    mCountriesInfoFromAPI.slice(1..20).forEach { item ->
                        mCountriesInfoToDB.add(
                            item.convertToCommonInfoDbItem()
                        )
                        mDataBaseCommonInfoRepository.addAll(mCountriesInfoToDB.convertToRoomDto())
                    }
                }
                .map { addDistanceToCountriesDtoList(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           mCountriesListLiveData.next(it)
                }, { }, {
                    if (mCountriesListLiveData.value is Outcome.Next) {
                        mCountriesListLiveData.success((mCountriesListLiveData.value as Outcome.Next).data)
                    }
                }
                )
        )
    }
}