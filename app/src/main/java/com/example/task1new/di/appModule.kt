package com.example.task1new.di

import com.example.domain.repository.NetworkRepository
import com.example.task1new.Retrofit
import com.example.task1new.repository.networkRepo.NetworkRepositoryImpl
import com.example.data.room.DBInfo
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { com.example.data.room.DBInfo.init(get()) }
    single{ Retrofit.COUNTRY_SERVICE }

    //Data level
    single{ NetworkRepositoryImpl(get()) as NetworkRepository }

}