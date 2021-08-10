package com.example.task1new.base.filter

import com.example.task1new.DTO_DEFAULT_DISTANCE_VALUE
import com.example.task1new.FILTER_ANY_COUNTRY_VALUE
import com.example.task1new.dto.CountryDto
import kotlin.math.max

object CountryDtoListFilterObject {

    private var mCountryName: String = FILTER_ANY_COUNTRY_VALUE

    private var mMinPopulation: Int = Integer.MIN_VALUE
    private var mMaxPopulation: Int = Integer.MAX_VALUE

    private var mMinArea: Double = Double.MIN_VALUE
    private var mMaxArea: Double = Double.MAX_VALUE

    private var mMaxDistance: Double = Double.MAX_VALUE

    private var mFilterIsEmpty: Boolean = true

    const val mDefaultCountryName: String = FILTER_ANY_COUNTRY_VALUE

    const val mDefaultMinPopulation: Int = Integer.MIN_VALUE
    const val mDefaultMaxPopulation: Int = Integer.MAX_VALUE
    const val mDefaultMinArea: Double = Double.MIN_VALUE
    const val mDefaultMaxArea: Double = Double.MAX_VALUE
    const val mDefaultMaxDistance: Double = Double.MAX_VALUE
    const val mDefaultFilterIsEmpty: Boolean = true

    fun List<CountryDto>.applyFilter(filter: CountryDtoListFilterObject): MutableList<CountryDto> {
        val result = mutableListOf<CountryDto>()
        for (country in this) {
            if (filter.checkForFilter(country)) {
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

    fun showFilterInfo() {
        println("NAME = $mCountryName")
        println("MIN POPULATION = $mMinPopulation")
        println("MAX POPULATION = $mMaxPopulation")
        println("MIN AREA = $mMinArea")
        println("MAX AREA = $mMaxArea")
        println("MAX DISTANCE = $mMaxDistance")
    }

    fun filterSetupChangeOptions(
        countryName: String = mCountryName,
        minPopulation: Int = mMinPopulation,
        maxPopulation: Int = mMaxPopulation,
        minArea: Double = mMinArea,
        maxArea: Double = mMaxArea,
        maxDistance: Double = mMaxDistance,
    ) {
        clearFilter()
        if (countryName != mCountryName) {
            setUpCountryName(countryName)
        }
        if (minPopulation != mMinPopulation) {
            setUpMinPopulation(minPopulation)
        }
        if (maxPopulation != mMaxPopulation) {
            setUpMaxPopulation(maxPopulation)
        }
        if (minArea != mMinArea) {
            setUpMinArea(minArea)
        }
        if (maxArea != mMaxArea) {
            setUpMaxArea(maxArea)
        }
        if (maxDistance != mMaxDistance) {
            setUpMaxDistance(mMaxDistance)
        }
    }

    private fun clearFilter() {
        mCountryName = FILTER_ANY_COUNTRY_VALUE
        mMinPopulation = Integer.MIN_VALUE
        mMaxPopulation = Integer.MAX_VALUE
        mMinArea = Double.MIN_VALUE
        mMaxArea = Double.MAX_VALUE
        mMaxDistance = Double.MAX_VALUE
        mFilterIsEmpty = true
    }

    fun setUpCountryName(countryName: String) {
        mCountryName = countryName
        mFilterIsEmpty = false
    }

    fun setUpMinPopulation(value: Int) {
        mMinPopulation = value
        mFilterIsEmpty = false
    }

    fun setUpMaxPopulation(value: Int) {
        mMaxPopulation = value
        mFilterIsEmpty = false
    }

    fun setUpMinArea(value: Double) {
        mMinArea = value
        mFilterIsEmpty = false
    }

    fun setUpMaxArea(value: Double) {
        mMaxArea = value
        mFilterIsEmpty = false
    }

    fun setUpMaxDistance(value: Double) {
        mMaxDistance = value
        mFilterIsEmpty = false
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
        return if (dto.distance == DTO_DEFAULT_DISTANCE_VALUE) {
            false
        } else dto.distance.toDouble() < mMaxDistance
    }
}