package com.example.task1new.di

import com.example.data.repository.databaseRepo.DatabaseCommonInfoRepositoryImpl
import com.example.data.repository.databaseRepo.DatabaseLanguageRepositoryImpl
import com.example.domain.repository.network.NetworkRepository
import com.example.task1new.Retrofit
import com.example.data.repository.networkRepo.NetworkRepositoryImpl
import com.example.domain.repository.database.DatabaseCommonInfoRepository
import com.example.domain.repository.database.DatabaseLanguageRepository
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { com.example.data.room.DBInfo.init(get()) }
    single{ Retrofit.COUNTRY_SERVICE }

    //Data level
    single{ NetworkRepositoryImpl(get()) as NetworkRepository }
    single{ DatabaseCommonInfoRepositoryImpl(get()) as DatabaseCommonInfoRepository}
    single{ DatabaseLanguageRepositoryImpl(get()) as DatabaseLanguageRepository}

}