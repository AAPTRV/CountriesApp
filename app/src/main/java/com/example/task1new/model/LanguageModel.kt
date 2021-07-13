package com.example.task1new.model

import com.example.task1new.dto.LanguageDto

data class LanguageModel(
    val iso639_1: String?,
    val iso639_2: String?,
    val name: String?,
    val nativeName: String?
) {
    override fun toString(): String {
        return name!!
    }

    fun convertToDto(): LanguageDto {

        var mDtoIso639_1 = "no iso 639_1"
        var mDtoIso639_2 = "no iso 639_2"
        var mDtoName = "no name"
        var mDtoNativeName = "No native name"

        if (!this.iso639_1.isNullOrEmpty()) {
            mDtoIso639_1 = this.iso639_1
        }
        if (!this.iso639_2.isNullOrEmpty()) {
            mDtoIso639_2 = this.iso639_2
        }
        if (!this.name.isNullOrEmpty()) {
            mDtoName = this.name
        }
        if (!this.nativeName.isNullOrEmpty()) {
            mDtoNativeName = this.nativeName
        }
        return LanguageDto(
            mDtoIso639_1, mDtoIso639_2, mDtoName, mDtoNativeName
        )
    }
}