package com.example.task1new.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CountryDatabaseCommonInfoEntity::class], version = 1)
abstract class DBInfo: RoomDatabase() {

    abstract fun getCountryCommonInfoDAO(): CountryCommonInfoDAO

    companion object{
        fun init(context: Context) =
            Room.databaseBuilder(context, DBInfo::class.java, "DB")
                .allowMainThreadQueries()
                .build()
    }
}