package com.example.task1new.ext

import com.example.domain.dto.LanguageDto
import com.example.task1new.room.CountryDatabaseLanguageInfoEntity

fun CountryDatabaseLanguageInfoEntity.convertToLanguagesDto(): MutableList<LanguageDto> {


    val mListOfLanguages = mutableListOf<LanguageDto>()

    // Создаём списки параметров для создания множества объектов Language из Entity

    val iso_639_1 = this.iso639_1.convertStringLanguageToListLanguage()
    val iso_639_2 = this.iso639_2.convertStringLanguageToListLanguage()
    val mName = this.mName.convertStringLanguageToListLanguage()
    val mNativeName = this.mNativeName.convertStringLanguageToListLanguage()

    val size = mName.size

    for (range in 1..size) {
        mListOfLanguages.add(
            LanguageDto(
                iso_639_1[range - 1],
                iso_639_2[range - 1],
                mName[range - 1],
                mNativeName[range - 1]
            )
        )
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



