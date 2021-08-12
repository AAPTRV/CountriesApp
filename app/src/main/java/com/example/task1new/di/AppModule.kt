package com.example.task1new.di

import com.example.data.interactor.CountryInteractorImpl
import com.example.data.network.Retrofit
import com.example.data.repository.cache.CacheRepositoryImpl
import com.example.data.room.DBInfo
import com.example.domain.repository.DatabaseRepository
import com.example.data.repository.database.DatabaseRepositoryImpl
import com.example.domain.repository.FilterRepository
import com.example.data.repository.filter.FilterRepositoryImpl
import com.example.domain.repository.NetworkRepository
import com.example.data.repository.networkRepo.NetworkRepositoryImpl
import com.example.domain.interactor.CountryInteractor
import com.example.domain.repository.CacheRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.math.sin

val appModule = module {

    //Model level
    single { DBInfo.init(get()) }
    single { DatabaseRepositoryImpl(get()) as DatabaseRepository }

    single { Retrofit.COUNTRY_SERVICE }
    single(named("retrofit2")) { Retrofit.COUNTRY_SERVICE }

    //Data level
    single { NetworkRepositoryImpl(get()) as NetworkRepository }
    single { FilterRepositoryImpl() as FilterRepository }
    single { CacheRepositoryImpl() as CacheRepository }

    single { CountryInteractorImpl(get(), get(), get(), get()) as CountryInteractor }

}