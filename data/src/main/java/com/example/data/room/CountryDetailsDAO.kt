package com.example.data.room

import androidx.room.*

@Dao
interface CountryDetailsDAO {
    @Query("SELECT * FROM countries_data_base_details_info WHERE name = :countryName")
    fun getDetailsInfoByCountry(countryName: String): CountryDatabaseDetailsEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: CountryDatabaseDetailsEntity)

    @Update
    fun update(entity: CountryDatabaseDetailsEntity)
}