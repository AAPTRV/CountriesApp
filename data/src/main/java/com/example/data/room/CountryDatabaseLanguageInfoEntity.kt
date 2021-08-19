package com.example.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import com.example.domain.dto.RoomCommonInfoDto
import com.example.domain.dto.RoomLanguageDto

fun List<CountryDatabaseLanguageInfoEntity>.convertToRoomDto(): List<RoomLanguageDto>{
    val result = mutableListOf<RoomLanguageDto>()
    this.forEach { result.add(it.convertToRoomDto()) }
    return result
}

fun CountryDatabaseLanguageInfoEntity.convertToRoomDto(): RoomLanguageDto{
    return RoomLanguageDto(
        this.mCountryName,
        this.iso639_1,
        this.iso639_2,
        this.mName,
        this.mNativeName
    )
}

fun List<RoomLanguageDto>.convertToEntity(): List<CountryDatabaseLanguageInfoEntity>{
    val result = mutableListOf<CountryDatabaseLanguageInfoEntity>()
    this.forEach { result.add(it.convertToEntity()) }
    return result
}

fun RoomLanguageDto.convertToEntity(): CountryDatabaseLanguageInfoEntity{
    return CountryDatabaseLanguageInfoEntity(
        this.mCountryName,
        this.iso639_1,
        this.iso639_2,
        this.mName,
        this.mNativeName
    )
}


@Entity(tableName = "countries_data_base_languages_info")
class CountryDatabaseLanguageInfoEntity(

    @PrimaryKey
    @ColumnInfo val mCountryName: String,
    @ColumnInfo val iso639_1: String,
    @ColumnInfo val iso639_2: String,
    @ColumnInfo val mName: String,
    @ColumnInfo val mNativeName: String

)