package com.example.task1new.transformer

import com.example.task1new.ext.convertToLanguages
import com.example.task1new.model.Languages
import com.example.task1new.model.PostCountryItem
import com.example.task1new.room.CountryDatabaseCommonInfoEntity
import com.example.task1new.room.CountryDatabaseLanguageInfoEntity

class DaoEntityToAPITransformer {
    companion object {
        fun daoEntityToApiTransformer(
            countryEntity: CountryDatabaseCommonInfoEntity,
            languageEntity: CountryDatabaseLanguageInfoEntity

        ): PostCountryItem {

            // Поля, которые нужно передать в конструктор для создания объекта PostCountryItem
            val mPostName: String = countryEntity.name
            val mPostCapital: String = countryEntity.capital
            val mPostPopulation: Int = countryEntity.population
            val mPostLanguages: List<Languages> = languageEntity.convertToLanguages()

            return PostCountryItem(mPostName, mPostCapital, mPostPopulation, mPostLanguages)
        }
    }
}