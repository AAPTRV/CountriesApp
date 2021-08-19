package com.example.data.room

import androidx.room.*

@Dao
interface CountryCommonInfoDAO {

    @Query("SELECT * FROM countries_data_base_table_info")
    fun getAllInfo(): List<CountryDatabaseCommonInfoEntity>

//    @Query("SELECT * , TOP (20) FROM countries_data_base_table_info")
//    fun getTop20Countries(): List<CountryDatabaseCommonInfoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: CountryDatabaseCommonInfoEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(list: List<CountryDatabaseCommonInfoEntity>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateAll(list: List<CountryDatabaseCommonInfoEntity>)

    @Delete
    fun deleteAll(list: List<CountryDatabaseCommonInfoEntity>)

    @Delete
    fun delete(entity: CountryDatabaseCommonInfoEntity)

}