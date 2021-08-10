package com.example.task1new.ext

import com.example.data.model.LanguageModel
import java.lang.StringBuilder

fun List<com.example.data.model.LanguageModel>.convertToCountryNameList(): String {
    val sb = StringBuilder()
    this.forEachIndexed { index, language ->
        sb.append(language.toString())
        if (index != this.size - 1 && this.size != 1)
            sb.append(", ")
    }
    return sb.toString()
}

fun List<Double?>.convertToCountryLocationList(): String {
    val sb = StringBuilder()
    this.forEachIndexed { index, language ->
        sb.append(language.toString())
        if (index != this.size - 1 && this.size != 1)
            sb.append(", ")
    }
    return sb.toString()
}

