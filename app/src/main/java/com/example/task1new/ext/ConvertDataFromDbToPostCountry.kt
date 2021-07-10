package com.example.task1new.ext

import com.example.task1new.model.Languages
import com.example.task1new.model.PostCountryItem
import com.example.task1new.room.CountryDatabaseCommonInfoEntity
import com.example.task1new.room.CountryDatabaseLanguageInfoEntity

fun CountryDatabaseLanguageInfoEntity.convertToLanguages(): MutableList<Languages> {

    val mListOfLanguages = mutableListOf<Languages>()

    // Создаём списки параметров для создания множества объектов Language из Entity

    val iso_639_1 = this.iso639_1.convertStringLanguageToListLanguage()
    val iso_639_2 = this.iso639_2.convertStringLanguageToListLanguage()
    val mName = this.mName.convertStringLanguageToListLanguage()
    val mNativeName = this.mNativeName.convertStringLanguageToListLanguage()

    val size = mName.size

    for (range in 1..size){
        mListOfLanguages.add(Languages(iso_639_1[range - 1], iso_639_2[range - 1], mName[range - 1], mNativeName[range - 1]))
    }
    return mListOfLanguages
}

fun String.convertStringLanguageToListLanguage(): List<String> {
    val list = mutableListOf<String>()
    if (!this.contains(",")) {
        list.add(this)
        return list
    }
    return this.split(",").toMutableList()
}


