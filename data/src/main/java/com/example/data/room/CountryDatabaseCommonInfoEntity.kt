package com.example.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.dto.RoomCommonInfoDto


// TODO: Make single extension class for all transformations ...
fun List<CountryDatabaseCommonInfoEntity>.convertToRoomDto(): List<RoomCommonInfoDto>{
    val result = mutableListOf<RoomCommonInfoDto>()
    this.forEach { result.add(it.convertToRoomDto()) }
    return result
}

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

fun List<RoomCommonInfoDto>.convertToEntity(): List<CountryDatabaseCommonInfoEntity>{
    val result = mutableListOf<CountryDatabaseCommonInfoEntity>()
    this.forEach { result.add(it.convertToEntity()) }
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

@Entity(tableName = "countries_data_base_table_info")
class CountryDatabaseCommonInfoEntity(

    @PrimaryKey
    @ColumnInfo
    val name: String,

    @ColumnInfo
    val capital: String,

    @ColumnInfo
    val population: Int,

    @ColumnInfo
    val languages: String,

    @ColumnInfo
    val flag: String,

    @ColumnInfo
    val area: Double,

    @ColumnInfo
    val location: String

)