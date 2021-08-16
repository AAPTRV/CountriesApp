package com.example.task1new.dto

import java.lang.StringBuilder

data class LanguageDto(
    val iso639_1: String,
    val iso639_2: String,
    val name: String,
    val nativeName: String
) {
    override fun toString(): String {
        return name
    }
}

fun List<LanguageDto>.convertLanguagesDtoToString(): String {

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