package com.example.data.ext

import com.example.data.model.LanguageModel
import java.lang.StringBuilder

fun List<LanguageModel?>.convertToCountryNameList(): String {
    val sb = StringBuilder()
    this.forEachIndexed { index, languageModel ->
        languageModel.let { language ->
            sb.append(language.toString())
            if (index != this.size - 1 && this.size != 1)
                sb.append(", ")
        }
    }
    return sb.toString()
}

fun List<Double?>.convertCountryListToString(): String {
    val sb = StringBuilder()
    this.forEachIndexed { index, language ->
        language?.let {
            sb.append(language.toString())
            if (index != this.size - 1 && this.size != 1)
                sb.append(", ")
        }
    }
    return sb.toString()
}

