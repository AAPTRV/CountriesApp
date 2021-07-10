package com.example.task1new.room

import androidx.room.*

@Dao
interface CountryCommonInfoDAO {

    @Query("SELECT * FROM countries_data_base_table_info")
    fun getAllInfo(): List<CountryDatabaseCommonInfoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: CountryDatabaseCommonInfoEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(list: List<CountryDatabaseCommonInfoEntity>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateAll(list: List<CountryDatabaseCommonInfoEntity>)

    @Delete
    fun deleteAll(list: List<CountryDatabaseCommonInfoEntity>)

    @Delete
    fun delete(enitiy: CountryDatabaseCommonInfoEntity)

}