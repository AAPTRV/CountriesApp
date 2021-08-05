package com.repository.filter

data class CountryFilter(
    var area: Float = Float.MAX_VALUE,
    var population: Float = Float.MAX_VALUE,
    var distanceToTheUser: Float = Float.MAX_VALUE,
    var name: String = ""
)