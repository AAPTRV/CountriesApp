package com.example.data.repository.networkRepo

import com.example.domain.dto.CountryDto
import com.example.data.model.convertToCountryDto
import com.example.data.network.CoroutinesCountryService
import com.example.domain.repository.network.NetworkRepository
import io.reactivex.rxjava3.core.Flowable

class NetworkRepositoryImpl(private val mService: com.example.data.network.CountryService, private val mCoroutinesService: CoroutinesCountryService) :
    NetworkRepository {

    override fun getCountryList(): Flowable<List<CountryDto>> =
        mService.getCountryList().map { it.convertToCountryDto() }

    override fun getCountryByName(name: String): Flowable<List<CountryDto>> =
        mService.getCountryByName(name).map { it.convertToCountryDto() }

}