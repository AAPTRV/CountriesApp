package com.example.task1new.transformer

import android.util.Log
import com.example.task1new.ext.convertToLanguagesDto
import com.example.task1new.dto.LanguageDto
import com.example.task1new.dto.CountryDto
import com.example.task1new.room.CountryDatabaseCommonInfoEntity
import com.example.task1new.room.CountryDatabaseLanguageInfoEntity

class DaoEntityToDtoTransformer {
    companion object {

        fun daoEntityToDtoTransformer(
            countryEntity: CountryDatabaseCommonInfoEntity,
            languageEntity: CountryDatabaseLanguageInfoEntity

        ): CountryDto {

            // Поля, которые нужно передать в конструктор для создания объекта PostCountryItem
            val mPostName: String = countryEntity.name
            val mPostCapital: String = countryEntity.capital
            val mPostPopulation: Int = countryEntity.population
            val mPostLanguages: MutableList<LanguageDto> = languageEntity.convertToLanguagesDto()
            val mPostFlag: String = countryEntity.flag
            val mPostArea: Double = countryEntity.area
            val mPostLocation: List<Double> = countryEntity.location.convertStringLocationToListLocation()

            return CountryDto(
                mPostName,
                mPostCapital,
                mPostPopulation,
                mPostLanguages,
                mPostFlag,
                mPostArea,
                mPostLocation
            )
        }
        fun String.convertStringLocationToListLocation(): List<Double> {
            val list = mutableListOf<Double>()
            if (!this.contains(",")) {
                return list
            }
            return this.split(",").map{it.toDouble()}.toMutableList()
        }

        fun List<CountryDatabaseLanguageInfoEntity>.findEntityByCountryName(countryName: String): CountryDatabaseLanguageInfoEntity{
            for(entity in this){
                if(entity.mCountryname.lowercase() == countryName.lowercase()){
                    return entity
                }
            }
            Log.e("ERROR", "ERROR OCCURED WHILE FINDING ENTITY BY COUNTRY NAME")
            return this[0]
        }
    }
}