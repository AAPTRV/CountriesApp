package com.example.domain.repository

import com.example.domain.dto.LanguageDto


interface DatabaseRepository {

    /**language*/
    fun getAllInfo(): List<LanguageDto>

    fun getLanguageInfoByCountry(countryName: String): List<LanguageDto>

//    fun add(entity: CountryDatabaseLanguageInfoEntity)
//
//    fun addAll(entities: List<CountryDatabaseLanguageInfoEntity>)
//
//    fun deleteAll(entities: List<CountryDatabaseLanguageInfoEntity>)

}