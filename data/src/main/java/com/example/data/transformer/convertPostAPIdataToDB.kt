package com.example.task1new.ext

import com.example.data.ext.convertCountryListToString
import com.example.data.ext.convertToCountryNameList
import com.example.domain.dto.CountryDto
import com.example.domain.dto.convertLanguagesDtoToString
import com.example.data.model.CountryModel
import java.lang.StringBuilder

fun CountryModel.convertCommonInfoAPIDatatoDBItem(): com.example.data.room.CountryDatabaseCommonInfoEntity {

    var mEntityName = "No name"
    var mEntityCapital = "No capital"
    var mEntityPopulation = 0
    var mEntityLanguages = "No languages"
    var mEntityFlag = ""
    var mEntityArea = 0.0
    var mEntityLocation = ""

    this.name?.let { mEntityName = this.name }
    this.capital?.let { mEntityCapital = this.capital }
    this.population?.let { mEntityPopulation = this.population }
    if (this.languages.isNotEmpty()) {
        mEntityLanguages = this.languages.convertToCountryNameList()
    }
    this.flag?.let { mEntityFlag = it }
    this.area?.let { mEntityArea = it }


    if(this.latlng.isNotEmpty()){
        mEntityLocation = this.latlng.convertCountryListToString()
    }

    return com.example.data.room.CountryDatabaseCommonInfoEntity(
        mEntityName,
        mEntityCapital,
        mEntityPopulation,
        mEntityLanguages,
        mEntityFlag,
        mEntityArea,
        mEntityLocation
    )
}

fun CountryDto.convertToCommonInfoDbItem(): com.example.data.room.CountryDatabaseCommonInfoEntity {
    return com.example.data.room.CountryDatabaseCommonInfoEntity(
        this.name,
        this.capital,
        this.population,
        this.languages.convertLanguagesDtoToString(),
        this.flag,
        this.area,
        this.location.convertCountryListToString()
    )
}

fun CountryDto.convertToLanguagesDbItem(): List<com.example.data.room.CountryDatabaseLanguageInfoEntity>{
    val result = mutableListOf<com.example.data.room.CountryDatabaseLanguageInfoEntity>()

    fun MutableList<String>.convertListToString(): String {
        val sb = StringBuilder()
        this.forEachIndexed { index, item ->
            if (this.size > 1 && index != this.size - 1) {
                sb.append(item)
                sb.append(",")
            } else {
                sb.append(item)
            }
        }
        return sb.toString()
    }

    for(language in this.languages){
        result.add(
            com.example.data.room.CountryDatabaseLanguageInfoEntity(
                this.name,
                language.iso639_1,
                language.iso639_2,
                language.name,
                language.nativeName
            )
        )
    }

    return result

}

fun CountryModel.convertLanguagesAPIDataToDBItem(): com.example.data.room.CountryDatabaseLanguageInfoEntity {

    fun MutableList<String>.convertListToString(): String {
        val sb = StringBuilder()
        this.forEachIndexed { index, item ->
            if (this.size > 1 && index != this.size - 1) {
                sb.append(item)
                sb.append(",")
            } else {
                sb.append(item)
            }
        }
        return sb.toString()
    }

    var mEntityCountryName = "No name"
    val mEntityIso6391 = mutableListOf<String>()
    val mEntityIso6392 = mutableListOf<String>()
    val mEntityName = mutableListOf<String>()
    val mEntityNativeName = mutableListOf<String>()

    if (!this.name.isNullOrEmpty()) {
        mEntityCountryName = this.name
    }

    for (LanguageModel in this.languages) {
        if (!LanguageModel.iso639_1.isNullOrEmpty()) {
            mEntityIso6391.add(LanguageModel.iso639_1)
        }
        if (!LanguageModel.iso639_2.isNullOrEmpty()) {
            mEntityIso6392.add(LanguageModel.iso639_2)
        }
        if (!LanguageModel.name.isNullOrEmpty()) {
            mEntityName.add(LanguageModel.name)
        }
        if (!LanguageModel.nativeName.isNullOrEmpty()) {
            mEntityNativeName.add(LanguageModel.nativeName)
        }
    }

    return com.example.data.room.CountryDatabaseLanguageInfoEntity(
        mEntityCountryName,
        mEntityIso6391.convertListToString(),
        mEntityIso6392.convertListToString(),
        mEntityName.convertListToString(),
        mEntityNativeName.convertListToString()
    )
}