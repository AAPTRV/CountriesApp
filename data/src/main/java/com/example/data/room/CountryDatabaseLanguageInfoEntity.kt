package com.example.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import com.example.domain.dto.RoomCommonInfoDto
import com.example.domain.dto.RoomLanguageDto


@Entity(tableName = "countries_data_base_languages_info")
class CountryDatabaseLanguageInfoEntity(

    @PrimaryKey
    @ColumnInfo val mCountryName: String,
    @ColumnInfo val iso639_1: String,
    @ColumnInfo val iso639_2: String,
    @ColumnInfo val mName: String,
    @ColumnInfo val mNativeName: String

)