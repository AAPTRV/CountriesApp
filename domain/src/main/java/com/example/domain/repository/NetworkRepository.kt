package com.example.domain.repository

import com.example.domain.dto.CountryDto
import io.reactivex.rxjava3.core.Flowable

interface NetworkRepository {

    fun getCountryList(): Flowable<List<CountryDto>>

    fun getCountryByName(name: String): Flowable<List<CountryDto>>

}