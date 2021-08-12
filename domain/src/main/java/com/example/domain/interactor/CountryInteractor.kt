package com.example.domain.interactor

import com.example.domain.dto.CountryDto
import io.reactivex.rxjava3.core.Flowable

interface CountryInteractor {

    fun getCountryChanel(): Flowable<MutableList<CountryDto>>

    fun generateAllCountries(): Flowable<Any>

    fun getCountriesByName(name: String): Flowable<Any>

}