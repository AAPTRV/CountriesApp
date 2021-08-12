package com.example.domain.repository

import com.example.domain.dto.CountryDto
import io.reactivex.rxjava3.core.Flowable


interface NetworkRepository {

    fun getAllCountries(): Flowable<MutableList<CountryDto>>

    fun getCountryByName(name: String): Flowable<MutableList<CountryDto>>

}