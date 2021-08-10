package com.example.task1new.app

import android.app.Application
import com.example.task1new.room.DBInfo

class CountriesApp : Application() {

    companion object {
        lateinit var mDatabase: DBInfo
    }

    override fun onCreate() {
        super.onCreate()
        mDatabase = this.let { DBInfo.init(it) }

    }
}