package com.example.task1new.ext

import com.example.task1new.model.PostCountryItemModel
import com.example.task1new.room.CountryDatabaseCommonInfoEntity
import com.example.task1new.room.CountryDatabaseLanguageInfoEntity
import java.lang.StringBuilder

fun PostCountryItemModel.convertCommonInfoAPIDatatoDBItem(): CountryDatabaseCommonInfoEntity{

    var mEntityName = "No name"
    var mEntityCapital = "No capital"
    var mEntityPopulation = 0
    var mEntityLanguages = "No languages"

    if(!this.name.isNullOrEmpty()){
        mEntityName = this.name
    }
    if(!this.capital.isNullOrEmpty()){
        mEntityCapital = this.capital
    }
    if(this.population != null){
        mEntityPopulation = this.population
    }
    if(!this.languages.isEmpty()){
        mEntityLanguages = this.languages.convertToCountryNameList()
    }
    return CountryDatabaseCommonInfoEntity(mEntityName, mEntityCapital, mEntityPopulation, mEntityLanguages)
}

fun PostCountryItemModel.convertLanguagesAPIDataToDBItem(): CountryDatabaseLanguageInfoEntity {

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

    if(!this.name.isNullOrEmpty()){
        mEntityCountryName = this.name
    }


    for (LanguageModel in this.languages) {
        if(!LanguageModel.iso639_1.isNullOrEmpty()){
            mEntityIso6391.add(LanguageModel.iso639_1)
        }
        if(!LanguageModel.iso639_2.isNullOrEmpty()){
            mEntityIso6392.add(LanguageModel.iso639_2)
        }
        if(!LanguageModel.name.isNullOrEmpty()){
            mEntityName.add(LanguageModel.name)
        }
        if(!LanguageModel.nativeName.isNullOrEmpty()){
            mEntityNativeName.add(LanguageModel.nativeName)
        }
    }

    return CountryDatabaseLanguageInfoEntity(
        mEntityCountryName, mEntityIso6391.convertListToString(),
        mEntityIso6392.convertListToString(), mEntityName.convertListToString(), mEntityNativeName.convertListToString()
    )
}