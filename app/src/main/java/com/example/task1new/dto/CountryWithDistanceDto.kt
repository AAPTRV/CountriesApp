package com.example.task1new.dto

import com.example.task1new.model.CountryModel

fun CountryDto.convertToCountryDtoWithDistance(
    distance: Double
): CountryWithDistanceDto {
    return CountryWithDistanceDto(
        name = this.name,
        capital = this.capital,
        population = this.population,
        languages = this.languages,
        flag = this.flag,
        area = this.area,
        location = this.location,
        distance = distance
    )
}

data class CountryWithDistanceDto(
    val name: String,
    val capital: String,
    val population: Int,
    val languages: MutableList<LanguageDto>,
    val flag: String,
    val area: Double,
    val location: List<Double>,
    val distance: Double
)