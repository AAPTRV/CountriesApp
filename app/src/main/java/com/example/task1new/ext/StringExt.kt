package com.example.task1new.ext

import com.example.task1new.dto.LanguageDto
import com.example.task1new.model.LanguageModel
import java.lang.StringBuilder

fun List<LanguageModel>.convertToCountryNameList(): String {
    val sb = StringBuilder()
    this.forEachIndexed { index, language ->
        sb.append(language.toString())
        if (index != this.size - 1 && this.size != 1)
            sb.append(", ")
    }
    return sb.toString()
}

