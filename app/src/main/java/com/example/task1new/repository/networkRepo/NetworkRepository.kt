package com.example.task1new.repository.networkRepo

import com.example.task1new.dto.CountryDto
import com.example.task1new.model.CountryModel
import com.example.task1new.utils.NetConstants
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkRepository {

    fun getCountryList(): Flowable<List<CountryDto>>

    fun getCountryByName(name: String): Flowable<List<CountryDto>>

}