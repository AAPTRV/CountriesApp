package com.example.task1new.di

import com.example.data.network.CoroutinesCountryService
import com.example.data.network.CountryService
import com.example.data.repository.databaseRepo.DatabaseCommonInfoRepositoryImpl
import com.example.data.repository.databaseRepo.DatabaseLanguageRepositoryImpl
import com.example.data.repository.networkRepo.NetworkRepositoryCoroutinesImpl
import com.example.domain.repository.network.NetworkRepository
import com.example.task1new.Retrofit
import com.example.data.repository.networkRepo.NetworkRepositoryImpl
import com.example.domain.repository.database.DatabaseCommonInfoRepository
import com.example.domain.repository.database.DatabaseLanguageRepository
import com.example.domain.repository.network.NetworkRepositoryCoroutines
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { com.example.data.room.DBInfo.init(get()) }
    single { Retrofit.getCountriesApi() }
    single { Retrofit.getCountriesCoroutinesApi() }

    //Data level
    single <NetworkRepositoryCoroutines>{ NetworkRepositoryCoroutinesImpl(get(), get()) }
    single <NetworkRepository>{ NetworkRepositoryImpl(get(), get())}
    single <DatabaseCommonInfoRepository>{ DatabaseCommonInfoRepositoryImpl(get())}
    single <DatabaseLanguageRepository>{ DatabaseLanguageRepositoryImpl(get())}

}