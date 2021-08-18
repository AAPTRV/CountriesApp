package com.example.task1new.repository.networkRepo

import com.example.domain.dto.CountryDto
import com.example.data.model.convertToCountryDto
import io.reactivex.rxjava3.core.Flowable

class NetworkRepositoryImpl(private val mService: com.example.data.network.CountryService) :
    com.example.domain.repository.NetworkRepository {

    override fun getCountryList(): Flowable<List<CountryDto>> =
        mService.getCountryList().map { it.convertToCountryDto() }

    override fun getCountryByName(name: String): Flowable<List<CountryDto>> =
        mService.getCountryByName(name).map { it.convertToCountryDto() }

}