package com.example.task1new.repository.networkRepo

import com.example.task1new.dto.CountryDto
import com.example.task1new.model.convertToCountryDto
import com.example.task1new.network.CountryService
import io.reactivex.rxjava3.core.Flowable

class NetworkRepositoryImpl(private val mService: CountryService) : NetworkRepository {

    override fun getCountryList(): Flowable<List<CountryDto>> =
        mService.getCountryList().map { it.convertToCountryDto() }

    override fun getCountryByName(name: String): Flowable<List<CountryDto>> =
        mService.getCountryByName(name).map { it.convertToCountryDto() }

}