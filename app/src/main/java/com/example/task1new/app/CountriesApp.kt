package com.example.task1new.app

import android.app.Application
import com.example.task1new.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CountriesApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Koin Android logger
            androidLogger()
            // inject Android context
            androidContext(this@CountriesApp)
            // use modules
            modules(appModule, countryListModule, countryDetailsModule, mapsModule, capitalsModule)
        }
    }
}