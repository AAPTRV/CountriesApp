package com.example.task1new.screens.countryList

import android.app.Application
import com.example.task1new.OkRetrofit
import com.example.task1new.app.CountriesApp
import com.example.task1new.base.mvp.BaseMvpPresenter
import com.example.task1new.dto.PostCountryItemDto
import com.example.task1new.ext.convertCommonInfoAPIDatatoDBItem
import com.example.task1new.ext.convertLanguagesAPIDataToDBItem
import com.example.task1new.model.convertToPostCountryItemDto
import com.example.task1new.room.*
import com.example.task1new.transformer.DaoEntityToDtoTransformer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CountryListPresenter(mDataBase: DBInfo) : BaseMvpPresenter<CountryListView>() {

    private var mDaoCountryInfo: CountryCommonInfoDAO = mDataBase.getCountryCommonInfoDAO()
    private var mDaoLanguageInfo: CountryLanguageDAO = mDataBase.getLanguageCommonInfoDAO()


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

    fun getDataFromRetrofitToRecycleAdapter() {
        getView()?.showProgress()
        addDisposable(
            inBackground(
                OkRetrofit.jsonPlaceHolderApi.getPosts()
            ).subscribe({ response ->
                getView()?.addNewUniqueItemsInRecycleAdapter(response.convertToPostCountryItemDto())
                // DB inserting data
                val mCountriesInfoFromAPI = response.toMutableList()
                val mCountriesInfoToDB = mutableListOf<CountryDatabaseCommonInfoEntity>()

                val mLanguagesFromApiToDB = mutableListOf<CountryDatabaseLanguageInfoEntity>()
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
            }, { throwable ->
                throwable.printStackTrace()
                getView()?.hideProgress()
            })
        )
    }

}