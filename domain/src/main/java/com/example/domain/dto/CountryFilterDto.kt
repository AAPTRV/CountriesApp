package com.example.domain.dto

data class CountryFilterDto(
    var area: Float = Float.MAX_VALUE,
    var population: Float = Float.MAX_VALUE,
    var distanceToTheUser: Float = Float.MAX_VALUE,
    var name: String = ""
)