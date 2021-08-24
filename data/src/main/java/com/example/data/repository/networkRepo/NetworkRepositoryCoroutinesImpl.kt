package com.example.data.repository.networkRepo

import com.example.data.ext.convertToDto
import com.example.data.network.CoroutinesCountryService
import com.example.domain.dto.CountryDto
import com.example.domain.dto.SingleCapitalDto
import com.example.domain.repository.network.NetworkRepositoryCoroutines

class NetworkRepositoryCoroutinesImpl (private val mService: com.example.data.network.CountryService, private val mCoroutinesService: CoroutinesCountryService) :

    NetworkRepositoryCoroutines {

    override suspend fun getCapitalsListCoroutines(): List<SingleCapitalDto> =
        mCoroutinesService.getCapitalsCoroutines().map { it.convertToDto() }

//    override suspend fun getCountryListCoroutines(): List<CountryDto> =
//        mCoroutinesService.getCountryListCoroutines().map { it.convertToCountryItemDto() }
//
//    override suspend fun getCountryByNameCoroutines(name: String): List<CountryDto> =
//        mCoroutinesService.getCountryByNameCoroutines(name).map { it.convertToCountryItemDto() }

}