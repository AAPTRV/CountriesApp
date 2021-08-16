package com.example.task1new.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries_data_base_table_info")
class CountryDatabaseCommonInfoEntity(

    @PrimaryKey
    @ColumnInfo
    val name: String,

    @ColumnInfo
    val capital: String,

    @ColumnInfo
    val population: Int,

    @ColumnInfo
    val languages: String,

    @ColumnInfo
    val flag: String,

    @ColumnInfo
    val area: Double,

    @ColumnInfo
    val location: String

)