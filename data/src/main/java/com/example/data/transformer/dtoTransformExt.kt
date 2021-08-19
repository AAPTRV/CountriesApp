package com.example.data.transformer

import com.example.domain.dto.CountryDto
import com.example.domain.dto.CountryWithDistanceDto

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