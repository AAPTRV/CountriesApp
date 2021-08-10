package com.example.domain.usecase.impl

import com.example.domain.dto.CountryDto
import com.example.domain.repository.NetworkRepository
import com.example.domain.usecase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetAllCountriesUseCase(private val networkRepository: NetworkRepository) : UseCase<Unit, List<CountryDto>>() {

    override fun buildFlowable(params: Unit?): Flowable<List<CountryDto>> = networkRepository.getCountryList()

    override val mIsParamsRequired: Boolean
        get() = false

}