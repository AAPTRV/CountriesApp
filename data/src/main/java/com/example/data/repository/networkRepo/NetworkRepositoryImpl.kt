package com.example.data.repository.networkRepo

import com.example.domain.dto.CountryDto
import com.example.data.model.convertToCountryDto
import com.example.data.network.CountryService
import io.reactivex.rxjava3.core.Flowable

class NetworkRepositoryImpl(private val mService: CountryService) : com.example.domain.repository.NetworkRepository {

    override fun getAllCountries(): Flowable<MutableList<CountryDto>> = mService.getCountryList().map { it.convertToCountryDto() }

    override fun getCountryByName(name: String): Flowable<MutableList<CountryDto>> =
        mService.getCountryByName(name).map { it.convertToCountryDto() }

}