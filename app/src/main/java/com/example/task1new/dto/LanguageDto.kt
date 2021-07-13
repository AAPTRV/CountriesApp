package com.example.task1new.dto

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