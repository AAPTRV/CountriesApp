package com.example.task1new.transformer

import com.example.task1new.ext.convertToLanguagesDto
import com.example.task1new.dto.LanguageDto
import com.example.task1new.dto.PostCountryItemDto
import com.example.task1new.dto.convertLanguagesDtoToString
import com.example.task1new.room.CountryDatabaseCommonInfoEntity
import com.example.task1new.room.CountryDatabaseLanguageInfoEntity

class DaoEntityToDtoTransformer {
    companion object {
        fun daoEntityToDtoTransformer(
            countryEntity: CountryDatabaseCommonInfoEntity,
            languageEntity: CountryDatabaseLanguageInfoEntity

        ): PostCountryItemDto {

            // Поля, которые нужно передать в конструктор для создания объекта PostCountryItem
            val mPostName: String = countryEntity.name
            val mPostCapital: String = countryEntity.capital
            val mPostPopulation: Int = countryEntity.population
            val mPostLanguages: List<LanguageDto> = languageEntity.convertToLanguagesDto()

            return PostCountryItemDto(
                mPostName,
                mPostCapital,
                mPostPopulation,
                mPostLanguages.convertLanguagesDtoToString()
            )
        }
    }
}