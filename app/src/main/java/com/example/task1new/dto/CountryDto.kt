package com.example.task1new.dto

import com.example.task1new.DTO_DEFAULT_DISTANCE_VALUE

data class CountryDto(
    val name: String,
    val capital: String,
    val population: Int,
    val languages: MutableList<LanguageDto>,
    val flag: String,
    val area: Double,
    val location: List<Double>,
    var distance: String = DTO_DEFAULT_DISTANCE_VALUE
)