package com.example.task1new.di

import com.example.task1new.Retrofit
import com.example.task1new.repository.networkRepo.NetworkRepository
import com.example.task1new.repository.networkRepo.NetworkRepositoryImpl
import com.example.task1new.room.DBInfo
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { DBInfo.init(get()) }
    single{ Retrofit.COUNTRY_SERVICE }

    //Data level
    single{ NetworkRepositoryImpl(get()) as NetworkRepository }

}