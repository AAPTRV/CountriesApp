package com.example.task1new.app

import android.app.Application
import com.example.task1new.di.appModule
import com.example.task1new.di.countryDetailsModule
import com.example.task1new.di.countryListModule
import com.example.task1new.room.DBInfo
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CountriesApp: Application() {

    companion object{
        lateinit var mDatabase: DBInfo
    }

    override fun onCreate() {
        super.onCreate()
        mDatabase = this.let { DBInfo.init(it) }
        startKoin {
            // Koin Android logger
            androidLogger()
            // inject Android context
            androidContext(this@CountriesApp)
            // use modules
            modules(appModule, countryListModule, countryDetailsModule)
        }
    }


}