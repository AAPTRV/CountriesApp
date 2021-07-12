package com.example.task1new.transformer

import com.example.task1new.ext.convertToLanguages
import com.example.task1new.dto.LanguagesDto
import com.example.task1new.dto.PostCountryItemDto
import com.example.task1new.room.CountryDatabaseCommonInfoEntity
import com.example.task1new.room.CountryDatabaseLanguageInfoEntity

class DaoEntityToAPITransformer {
    companion object {
        fun daoEntityToApiTransformer(
            countryEntity: CountryDatabaseCommonInfoEntity,
            languageEntity: CountryDatabaseLanguageInfoEntity

        ): PostCountryItemDto {

            // Поля, которые нужно передать в конструктор для создания объекта PostCountryItem
            val mPostName: String = countryEntity.name
            val mPostCapital: String = countryEntity.capital
            val mPostPopulation: Int = countryEntity.population
            val mPostLanguages: List<LanguagesDto> = languageEntity.convertToLanguages()

            return PostCountryItemDto(mPostName, mPostCapital, mPostPopulation, mPostLanguages)
        }
    }
}