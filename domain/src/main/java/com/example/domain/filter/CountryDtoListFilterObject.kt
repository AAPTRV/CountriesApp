package com.example.domain.filter

import com.example.task1new.DTO_DEFAULT_DISTANCE_VALUE
import com.example.task1new.FILTER_ANY_COUNTRY_VALUE
import com.example.domain.dto.CountryDto

object CountryDtoListFilterObject {

    private var mCountryName: String = FILTER_ANY_COUNTRY_VALUE
    private var mMinPopulation: Int = Integer.MIN_VALUE
    private var mMaxPopulation: Int = Integer.MAX_VALUE
    private var mMinArea: Double = Double.MIN_VALUE
    private var mMaxArea: Double = Double.MAX_VALUE
    private var mMaxDistance: Double = Double.MAX_VALUE

    const val mDefaultMinPopulation: Int = Integer.MIN_VALUE
    const val mDefaultMaxPopulation: Int = Integer.MAX_VALUE
    const val mDefaultMinArea: Double = Double.MIN_VALUE
    const val mDefaultMaxArea: Double = Double.MAX_VALUE
    const val mDefaultMaxDistance: Double = Double.MAX_VALUE

    fun List<CountryDto>.applyFilter(filter: CountryDtoListFilterObject): MutableList<CountryDto> {
        val result = mutableListOf<CountryDto>()
        for (country in this) {
            if (checkForFilter(country)) {
                result.add(country)
            }
        }
        return result
    }

    fun filterClearExceptName() {
        mMaxPopulation = mDefaultMaxPopulation
        mMinPopulation = mDefaultMinPopulation
        mMaxArea = mDefaultMaxArea
        mMinArea = mDefaultMinArea
        mMaxDistance = mDefaultMaxDistance
    }

    fun filterCountryNameChange(countryName: String) {
        mCountryName = countryName
    }

    fun filterMaxPopulationChange(maxPopulation: Int) {
        mMaxPopulation = maxPopulation
    }

    fun filterMinPopulationChange(minPopulation: Int) {
        mMinPopulation = minPopulation
    }

    fun filterMaxAreaChange(maxArea: Double) {
        mMaxArea = maxArea
    }

    fun filterMinAreaChange(minArea: Double) {
        mMinArea = minArea
    }

    fun filterMaxDistanceChange(maxDistance: Double) {
        mMaxDistance = maxDistance
    }

    private fun checkForFilter(country: CountryDto): Boolean {
        if (checkCountryName(country) && checkPopulation(country) && checkArea(country) && checkDistance(
                country
            )
        ) {
            return true
        }
        return false
    }

    private fun checkCountryName(country: CountryDto): Boolean {
        if (country.name.lowercase()
                .contains(mCountryName.lowercase()) || mCountryName == FILTER_ANY_COUNTRY_VALUE
        ) {
            return true
        }
        return false
    }

    private fun checkPopulation(country: CountryDto): Boolean {
        if (country.population in mMinPopulation..mMaxPopulation) {
            return true
        }
        return false
    }

    private fun checkArea(country: CountryDto): Boolean {
        if (country.area in mMinArea..mMaxArea) {
            return true
        }
        return false
    }

    private fun checkDistance(dto: CountryDto): Boolean {
        if(dto.distance == DTO_DEFAULT_DISTANCE_VALUE){
            return false
        }
        if(dto.distance.toDouble() > mMaxDistance){
            return false
        }
        return true
    }
}