package com.example.task1new.dto

import com.example.task1new.model.PostCountryItemModel
import java.lang.StringBuilder

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

data class PostCountryItemDto(
    val name: String,
    val capital: String,
    val population: Int,
    val languages: MutableList<LanguageDto>,
    val flag: String
)