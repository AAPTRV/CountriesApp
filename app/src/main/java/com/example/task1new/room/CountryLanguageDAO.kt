package com.example.task1new.room

import androidx.room.*

@Dao
interface CountryLanguageDAO {

    @Query("SELECT * FROM countries_data_base_languages_info")
    fun getAllInfo(): List<CountryDatabaseLanguageInfoEntity>

    @Query("SELECT * FROM countries_data_base_languages_info WHERE mCountryname = :countryName")
    fun getLanguageInfoByCountry(countryName: String): CountryDatabaseLanguageInfoEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: CountryDatabaseLanguageInfoEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(entities: List<CountryDatabaseLanguageInfoEntity>)

    @Delete
    fun deleteAll(entities: List<CountryDatabaseLanguageInfoEntity>)

}