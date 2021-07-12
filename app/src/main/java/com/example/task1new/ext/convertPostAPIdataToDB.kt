package com.example.task1new.ext

import com.example.task1new.dto.PostCountryItemDto
import com.example.task1new.room.CountryDatabaseLanguageInfoEntity
import java.lang.StringBuilder

fun PostCountryItemDto.convertLanguagesAPIDataToDBItem(): CountryDatabaseLanguageInfoEntity {

    fun MutableList<String>.convertListToString(): String {
        val sb = StringBuilder()
        this.forEachIndexed { index, item ->
            if (this.size > 1 && index != this.size - 1) {  // Тут нужно в норму привести - без велосипеда
                sb.append(item)
                sb.append(",")
            } else {
                sb.append(item)
            }
        }
        return sb.toString()
    }

    val countryName = this.name
    val iso6391 = mutableListOf<String>()
    val iso6392 = mutableListOf<String>()
    val name = mutableListOf<String>()
    val nativeName = mutableListOf<String>()

    for (Language in this.languages) {
        iso6391.add(Language.iso639_1)
        iso6392.add(Language.iso639_2)
        name.add(Language.name)
        nativeName.add(Language.nativeName)
    }

    return CountryDatabaseLanguageInfoEntity(
        countryName, iso6391.convertListToString(),
        iso6392.convertListToString(), name.convertListToString(), nativeName.convertListToString()
    )
}