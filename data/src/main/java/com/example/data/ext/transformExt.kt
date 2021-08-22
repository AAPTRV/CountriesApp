package com.example.data.ext

import com.example.data.room.CountryDatabaseCommonInfoEntity
import com.example.data.room.CountryDatabaseLanguageInfoEntity
import com.example.domain.dto.RoomCommonInfoDto
import com.example.domain.dto.RoomLanguageDto

fun CountryDatabaseCommonInfoEntity.convertToRoomDto(): RoomCommonInfoDto{
    return RoomCommonInfoDto(
        this.name,
        this.capital,
        this.population,
        this.languages,
        this.flag,
        this.area,
        this.location
    )
}

fun List<CountryDatabaseCommonInfoEntity>.convertInfoToRoomDto(): List<RoomCommonInfoDto>{
    val result = mutableListOf<RoomCommonInfoDto>()
    this.forEach { result.add(it.convertToRoomDto()) }
    return result
}

fun RoomCommonInfoDto.convertToEntity(): CountryDatabaseCommonInfoEntity{
    return CountryDatabaseCommonInfoEntity(
        this.name,
        this.capital,
        this.population,
        this.languages,
        this.flag,
        this.area,
        this.location
    )
}

fun List<RoomCommonInfoDto>.convertInfoToEntity(): List<CountryDatabaseCommonInfoEntity>{
    val result = mutableListOf<CountryDatabaseCommonInfoEntity>()
    this.forEach { result.add(it.convertToEntity()) }
    return result
}



fun List<CountryDatabaseLanguageInfoEntity>.convertLanguageToRoomDto(): List<RoomLanguageDto>{
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

fun List<RoomLanguageDto>.convertLanguageToEntity(): List<CountryDatabaseLanguageInfoEntity>{
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