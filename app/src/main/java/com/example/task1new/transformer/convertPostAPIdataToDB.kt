package com.example.task1new.ext

import com.example.data.model.CountryModel
import com.example.data.room.CountryDatabaseCommonInfoEntity
import com.example.data.room.CountryDatabaseLanguageInfoEntity
import org.koin.android.ext.koin.ERROR_MSG
import java.lang.StringBuilder

fun CountryModel.convertCommonInfoAPIDatatoDBItem(): CountryDatabaseCommonInfoEntity {

    var mEntityName = "No name"
    var mEntityCapital = "No capital"
    var mEntityPopulation = 0
    var mEntityLanguages = "No languages"
    var mEntityFlag = ""
    var mEntityArea = 0.0
    var mEntityLocation = ""

    this.name?.let { mEntityName = it }
    this.capital?.let { mEntityCapital = it }
    this.population?.let { mEntityPopulation = it }
    if (this.languages.isNotEmpty()) {
        mEntityLanguages = this.languages.convertToCountryNameList()
    }
    this.flag?.let { mEntityFlag = it }
    this.area?.let { mEntityArea = it }


    if (this.latlng.isNotEmpty()) {
        mEntityLocation = this.latlng.convertToCountryLocationList()
    }

    return CountryDatabaseCommonInfoEntity(
        mEntityName,
        mEntityCapital,
        mEntityPopulation,
        mEntityLanguages,
        mEntityFlag,
        mEntityArea,
        mEntityLocation
    )
}

fun com.example.data.model.CountryModel.convertLanguagesAPIDataToDBItem(): CountryDatabaseLanguageInfoEntity {

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

    // TODO: 10.08.2021 remove ""
    if (!this.name.isNullOrEmpty()) {
        mEntityCountryName = this.name ?: ""
    }

    for (LanguageModel in this.languages) {
        if (!LanguageModel.iso639_1.isNullOrEmpty()) {
            mEntityIso6391.add(LanguageModel.iso639_1 ?: "")
        }
        if (!LanguageModel.iso639_2.isNullOrEmpty()) {
            mEntityIso6392.add(LanguageModel.iso639_2 ?: "")
        }
        if (!LanguageModel.name.isNullOrEmpty()) {
            mEntityName.add(LanguageModel.name ?: "")
        }
        if (!LanguageModel.nativeName.isNullOrEmpty()) {
            mEntityNativeName.add(LanguageModel.nativeName ?: "")
        }
    }

    return CountryDatabaseLanguageInfoEntity(
        mEntityCountryName,
        mEntityIso6391.convertListToString(),
        mEntityIso6392.convertListToString(),
        mEntityName.convertListToString(),
        mEntityNativeName.convertListToString()
    )
}