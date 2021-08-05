package com.repository.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.task1new.dto.CountryDto
import com.example.task1new.dto.LanguageDto
import com.example.task1new.room.CountryDatabaseLanguageInfoEntity

interface DatabaseRepository {

    /**language*/
    fun getAllInfo(): List<LanguageDto>

    fun getLanguageInfoByCountry(countryName: String): List<LanguageDto>

    fun add(entity: CountryDatabaseLanguageInfoEntity)

    fun addAll(entities: List<CountryDatabaseLanguageInfoEntity>)

    fun deleteAll(entities: List<CountryDatabaseLanguageInfoEntity>)

}