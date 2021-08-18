package com.example.domain.dto

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
) {
    fun convertToLatLngDto(): LatLngDto {
        return if (this.location.isNotEmpty()) {
            LatLngDto(
                this.name, this.location[0], this.location[1]
            )
        } else {
            LatLngDto(this.name, 0.0, 0.0)
        }
    }
}