package com.example.task1new.screens.countryList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.data.room.*
import com.example.task1new.base.filter.CountryDtoListFilterObject
import com.example.task1new.base.mvvm.BaseViewModel
import com.example.domain.dto.CountryDto
import com.example.domain.interactor.CountryInteractor
import com.example.task1new.transformer.DaoEntityToDtoTransformer
import com.example.domain.repository.DatabaseRepository
import com.example.domain.repository.FilterRepository
import com.example.domain.usecase.impl.GetAllCountriesUseCase
import com.example.task1new.base.mvvm.Outcome
import com.example.task1new.base.mvvm.executeJob
import com.example.task1new.base.mvvm.executeJobWithoutProgress
import io.reactivex.rxjava3.schedulers.Schedulers

class CountryListViewModel(
    savedStateHandle: SavedStateHandle,
    mDataBase: DBInfo,
    private val mDatabaseRepository: DatabaseRepository,
    private val mGetAllCountriesUseCase: GetAllCountriesUseCase,
    private val mFilterRepository: FilterRepository,
    private val mCountryInteractor: CountryInteractor
) : BaseViewModel(savedStateHandle) {


    //private var mDaoCountryInfo: CountryCommonInfoDAO = mDataBase.getCountryCommonInfoDAO()
    //private var mDaoLanguageInfo: CountryLanguageDAO = mDataBase.getLanguageCommonInfoDAO()

    private val mCountryItemLiveData =
        savedStateHandle.getLiveData<CountryDto>("countryDto")
    private val mCountriesListLiveData =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDto>>>("countryListDto")
    private val mCountriesFilteredListLiveData =
        savedStateHandle.getLiveData<List<CountryDto>>("countryFilteredListDto")

    private val mCountriesFilterLiveData =
        savedStateHandle.getLiveData<CountryDtoListFilterObject>("countryListDtoFilter")

    init {
        mFilterRepository.getFilterSubject().subscribe({
            Log.e("hz", it.toString())
        }, {

        })

        mFilterRepository.processNewQuery("abc")
        mFilterRepository.processNewQuery("bcd")
    }

    fun getCountryLiveData(): MutableLiveData<CountryDto> {
        return mCountryItemLiveData
    }

    fun applyFilter(
        countryName: String,
        minPopulation: Int,
        maxPopulation: Int,
        minArea: Double,
        maxArea: Double,
        maxDistance: Double
    ) {
        CountryDtoListFilterObject.filterSetupChangeOptions(
            countryName = countryName,
            minPopulation = minPopulation,
            maxPopulation = maxPopulation,
            minArea = minArea,
            maxArea = maxArea,
            maxDistance = maxDistance,
        )
        mCountriesFilterLiveData.value = CountryDtoListFilterObject
    }

    fun getCountriesListLiveData(): MutableLiveData<Outcome<MutableList<CountryDto>>> {
        return mCountriesListLiveData
    }

    fun getCountriesFromDb() {
        val mCountriesLanguageEntities = mutableListOf<CountryDatabaseLanguageInfoEntity>()
        val mCountriesInfoEntities = mutableListOf<CountryDatabaseCommonInfoEntity>()
        val mPostCountriesData = mutableListOf<CountryDto>()
        // Filling mCountriesInfoEntities list with items from DB
        // TODO: 12.08.2021 replace with db repo  correct info
        mDatabaseRepository.getAllInfo().forEach { entity ->
            mCountriesInfoEntities.add(CountryDatabaseCommonInfoEntity(entity.name, "", 1, "", "", 1.0, ""))
        }
        // Filling mCountriesLanguageEntities with items from DB
        mCountriesInfoEntities.forEach { entity ->
            // TODO: 12.08.2021 replace with db repo  correct info
            val item = mDatabaseRepository.getLanguageInfoByCountry(entity.name)[0]
            mCountriesLanguageEntities.add(
                CountryDatabaseLanguageInfoEntity(
                    item.name,
                    item.iso639_1,
                    item.iso639_2,
                    item.nativeName,
                    item.nativeName
                )
            )
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
            mCountriesListLiveData.value = Outcome.next(mPostCountriesData)
        }
//        Log.e(
//            TAG,
//            "GOT COUNTRIES FROM DB. SIZE = ${mPostCountriesData.size}.  LIVE DATA SIZE = ${mCountriesListLiveData.value?.size}"
//        )
    }

    fun subscribeToCountryChannel() {
        mCompositeDisposable.add(executeJobWithoutProgress(mCountryInteractor.getCountryChanel(), mCountriesListLiveData))
    }

    fun getCountries() {
        mCompositeDisposable.add(
            executeJob(
                mCountryInteractor.generateAllCountries().map { mutableListOf() },
                mCountriesListLiveData
            )
        )
//                .doOnNext {
//                    // DB inserting data
//                    val mCountriesInfoFromAPI = it.toMutableList()
//                    val mCountriesInfoToDB = mutableListOf<CountryDatabaseCommonInfoEntity>()
//
//                    val mLanguagesFromApiToDB =
//                        mutableListOf<CountryDatabaseLanguageInfoEntity>()
////                    mCountriesInfoFromAPI.slice(1..20).forEach { item ->
////                        mLanguagesFromApiToDB.add(item.convertLanguagesAPIDataToDBItem())
////                    }
//                    //todo add dto related method to repo
//                    //mDatabaseRepository.addAll(mLanguagesFromApiToDB)
//
//                    mCountriesInfoFromAPI.slice(1..20).forEach { item ->
////                        mCountriesInfoToDB.add(
////                            item.convertCommonInfoAPIDatatoDBItem()
////                        )
//                        // TODO: 12.08.2021 replace with db repo
//                        //mDaoCountryInfo.addAll(mCountriesInfoToDB)
//                    }
//                    Log.e(TAG, "GET COUNTRIES FROM API TO DB")
//                }
        //.map { it.convertToCountryDto() }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ countriesDtoList -> }, { getCountriesFromDb() }
//            )
//        )
    }
}