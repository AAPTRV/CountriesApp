package com.example.domain.repository.database

import com.example.domain.dto.RoomCommonInfoDto
import com.example.domain.dto.RoomLanguageDto

interface DatabaseLanguageRepository {

    fun getAllInfo(): List<RoomLanguageDto>

    fun getLanguageInfoByCountry(countryName: String): RoomLanguageDto

    fun add(entityToDto: RoomLanguageDto)

    fun addAll(entitiesToDto: List<RoomLanguageDto>)

    fun deleteAll(entitiesToDto: List<RoomLanguageDto>)

}