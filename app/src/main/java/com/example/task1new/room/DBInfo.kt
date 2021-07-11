package com.example.task1new.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CountryDatabaseCommonInfoEntity::class, CountryDatabaseLanguageInfoEntity::class],
    version = DBInfo.LATEST_VERSION
)
abstract class DBInfo : RoomDatabase() {

    abstract fun getCountryCommonInfoDAO(): CountryCommonInfoDAO
    abstract fun getLanguageCommonInfoDAO(): CountryLanguageDAO

    companion object {

        const val LATEST_VERSION = 3

        fun init(context: Context) =
            Room.databaseBuilder(context, DBInfo::class.java, "DB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }
}