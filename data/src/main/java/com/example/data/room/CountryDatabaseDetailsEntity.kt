package com.example.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries_data_base_details_info")
class CountryDatabaseDetailsEntity(

    @PrimaryKey
    @ColumnInfo
    val name: String,

    @ColumnInfo
    val note: String
)