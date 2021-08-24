package com.example.domain.repository.network

import com.example.domain.dto.CountryDto
import com.example.domain.dto.SingleCapitalDto
import io.reactivex.rxjava3.core.Flowable

interface NetworkRepository {

    fun getCountryList(): Flowable<List<CountryDto>>

    fun getCountryByName(name: String): Flowable<List<CountryDto>>

    suspend fun getCapitalsListCoroutines(): List<SingleCapitalDto>

    suspend fun getCountryListCoroutines(): List<CountryDto>

    suspend fun getCountryByNameCoroutines(name: String): List<CountryDto>

}