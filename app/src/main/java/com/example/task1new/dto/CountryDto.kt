package com.example.task1new.dto

data class CountryDto(
    val name: String,
    val capital: String,
    val population: Int,
    val languages: MutableList<LanguageDto>,
    val flag: String,
    val area: Double,
    val location: List<Double>,
    val distance: String = "Failed to investigate users location"
)