package com.example.task1new.model

import com.example.task1new.dto.LanguageDto

data class LanguageModel(
    val iso639_1: String?,
    val iso639_2: String?,
    val name: String?,
    val nativeName: String?
) {
    override fun toString(): String {
        return name ?: ""
    }

    fun convertToDto(): LanguageDto {

        var mDtoIso6391 = "no iso 639_1"
        var mDtoIso6392 = "no iso 639_2"
        var mDtoName = "no name"
        var mDtoNativeName = "No native name"

        this.iso639_1?.let { mDtoIso6391 = it }
        this.iso639_2?.let { mDtoIso6392 = it }
        this.name?.let { mDtoName = it }
        this.nativeName?.let { mDtoNativeName = it }

        return LanguageDto(
            mDtoIso6391, mDtoIso6392, mDtoName, mDtoNativeName
        )
    }
}