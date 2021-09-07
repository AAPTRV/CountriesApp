package com.example.domain.usecase.impl

import com.example.domain.dto.CountryDto
import com.example.domain.repository.network.NetworkRepository
import com.example.domain.usecase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetAllCountriesUseCase(private val mNetworkRepository: NetworkRepository ) : UseCase<Unit, List<CountryDto>>(){

    override val mIsParamsRequired: Boolean
        get() = false
    override fun buildFlowable(params: Unit?): Flowable<List<CountryDto>> = mNetworkRepository.getCountryList()
}