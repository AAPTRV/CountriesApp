package com.example.domain.usecase.impl

import com.example.domain.dto.CountryDto
import com.example.domain.repository.network.NetworkRepository
import com.example.domain.usecase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetCountryByNameUseCase(private val mNetworkRepository: NetworkRepository) : UseCase<String, List<CountryDto>>() {
    override val mIsParamsRequired: Boolean
        get() = true

    override fun buildFlowable(params: String?): Flowable<List<CountryDto>> = mNetworkRepository.getCountryByName(params ?: "")

}