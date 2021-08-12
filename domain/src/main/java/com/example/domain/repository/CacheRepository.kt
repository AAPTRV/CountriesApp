package com.example.domain.repository

import com.example.domain.dto.CountryDto
import io.reactivex.rxjava3.core.Flowable

interface CacheRepository {

    fun addAllCountries(list: MutableList<CountryDto>): Flowable<MutableList<CountryDto>>

    fun addFilteredCountries(list: MutableList<CountryDto>): Flowable<MutableList<CountryDto>>

    fun getAllCountries(): Flowable<MutableList<CountryDto>>

    fun getFilteredCountries(): Flowable<MutableList<CountryDto>>

}