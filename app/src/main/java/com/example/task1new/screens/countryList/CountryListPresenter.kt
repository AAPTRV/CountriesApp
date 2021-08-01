package com.example.task1new.screens.countryList

import com.example.task1new.Retrofit
import com.example.task1new.base.mvp.BaseMvpPresenter
import com.example.task1new.dto.PostCountryItemDto
import com.example.task1new.ext.convertCommonInfoAPIDatatoDBItem
import com.example.task1new.ext.convertLanguagesAPIDataToDBItem
import com.example.task1new.model.convertToCountryDto
import com.example.task1new.room.*
import com.example.task1new.transformer.DaoEntityToDtoTransformer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class CountryListPresenter(mDataBase: DBInfo) : BaseMvpPresenter<CountryListView>() {

    private var mDaoCountryInfo: CountryCommonInfoDAO = mDataBase.getCountryCommonInfoDAO()
    private var mDaoLanguageInfo: CountryLanguageDAO = mDataBase.getLanguageCommonInfoDAO()
    private var mSearchSubject = BehaviorSubject.create<String>()

    init {
        setupSearchSubject()
    }

    fun getDataFromDBToRecycleAdapter() {
        //BD reading data (initializing variables for entities)
        val mCountriesLanguageEntities = mutableListOf<CountryDatabaseLanguageInfoEntity>()
        val mCountriesInfoEntities = mutableListOf<CountryDatabaseCommonInfoEntity>()
        val mPostCountriesData = mutableListOf<PostCountryItemDto>()
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
            getView()?.addNewUniqueItemsInRecycleAdapter(mPostCountriesData)
        }
    }

    private fun setupSearchSubject() {
        addDisposable(mSearchSubject
            .filter { it.length >= 3 }
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .map { it.lowercase() }
            .flatMap {
                Retrofit.jsonPlaceHolderApi.getCountryByName(it).toObservable()
                    .onErrorResumeNext { io.reactivex.rxjava3.core.Observable.just(mutableListOf()) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { getView()?.repopulateFilteredDataListInAdapter(data = it.convertToCountryDto()) },
                { it.message?.let { text -> getView()?.showError(text, it) } })
        )
    }

    fun getSearchSubject(): BehaviorSubject<String> {
        return mSearchSubject
    }

    fun getDataFromRetrofitToRecycleAdapter(isRefresh: Boolean) {
        if (!isRefresh) {
            getView()?.showProgress()
        }
        addDisposable(
            inBackground(
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
                    .map { it.convertToCountryDto() }
            ).subscribe({ dto ->
                getView()?.addNewUniqueItemsInRecycleAdapter(dto)
                if (!isRefresh) {
                    getView()?.hideProgress()
                }

            }, { throwable ->
                throwable.printStackTrace()
                if (!isRefresh) {
                    getView()?.hideProgress()
                }
            })
        )
    }
}