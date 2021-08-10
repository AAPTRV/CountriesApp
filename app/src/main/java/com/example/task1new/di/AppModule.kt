package com.example.task1new.di

import com.example.data.network.Retrofit
import com.example.task1new.room.DBInfo
import com.example.domain.repository.DatabaseRepository
import com.repository.database.DatabaseRepositoryImpl
import com.example.domain.repository.FilterRepository
import com.example.domain.filter.FilterRepositoryImpl
import com.example.domain.repository.NetworkRepository
import com.repository.networkRepo.NetworkRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { DBInfo.init(get()) }
    single { DatabaseRepositoryImpl(get()) as DatabaseRepository }

    single { Retrofit.COUNTRY_SERVICE }
    single(named("retrofit2")) { Retrofit.COUNTRY_SERVICE }

    //Data level
    single { NetworkRepositoryImpl(get()) as NetworkRepository }
    single { FilterRepositoryImpl() as FilterRepository }

}