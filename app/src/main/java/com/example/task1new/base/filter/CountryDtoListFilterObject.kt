package com.example.task1new.base.filter

import com.example.domain.dto.CountryDto

object CountryDtoListFilterObject {

    private var mCountryName: String = "AnyCountry"

    private var mMinPopulation: Int = Integer.MIN_VALUE
    private var mMaxPopulation: Int = Integer.MAX_VALUE

    private var mMinArea: Double = Double.MIN_VALUE
    private var mMaxArea: Double = Double.MAX_VALUE

    private var mMaxDistance: Double = Double.MAX_VALUE

    private var mFilterIsEmpty: Boolean = true

    fun List<CountryDto>.applyFilter(filter: CountryDtoListFilterObject): MutableList<CountryDto> {
        val result = mutableListOf<CountryDto>()
        for (country in this) {
            if (filter.checkForFilter(country)) {
                result.add(country)
            }
        }
        return result
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
        mCountryName = "AnyCountry"
        mMinPopulation = Integer.MIN_VALUE
        mMaxPopulation = Integer.MAX_VALUE
        mMinArea = Double.MIN_VALUE
        mMaxArea = Double.MAX_VALUE
        mMaxDistance = Double.MAX_VALUE
        mFilterIsEmpty = true
    }

    private fun setUpCountryName(countryName: String) {
        mCountryName = countryName
        mFilterIsEmpty = false
    }

    private fun setUpMinPopulation(value: Int) {
        mMinPopulation = value
        mFilterIsEmpty = false
    }

    private fun setUpMaxPopulation(value: Int) {
        mMaxPopulation = value
        mFilterIsEmpty = false
    }

    private fun setUpMinArea(value: Double) {
        mMinArea = value
        mFilterIsEmpty = false
    }

    private fun setUpMaxArea(value: Double) {
        mMaxArea = value
        mFilterIsEmpty = false
    }

    private fun setUpMaxDistance(value: Double) {
        mMaxDistance = value
        mFilterIsEmpty = false
    }

    private fun checkForFilter(country: CountryDto): Boolean {
        if (checkCountryName(country) && checkPopulation(country) && checkArea(country) && checkDistance(country)) {
            return true
        }
        return false
    }

    private fun checkCountryName(country: CountryDto): Boolean {
        if (country.name.lowercase().contains(mCountryName.lowercase()) || mCountryName == "AnyCountry") {
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

    private fun checkDistance(distance: CountryDto): Boolean {
        return true
    }
}