package com.example.domain.usecase.impl

import com.example.domain.dto.CountryDto
import com.example.domain.repository.NetworkRepository
import com.example.domain.usecase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetAllCountriesUseCase(private val networkRepository: NetworkRepository) : UseCase<Unit, MutableList<CountryDto>>() {

    override fun buildFlowable(params: Unit?): Flowable<MutableList<CountryDto>> = networkRepository.getAllCountries()

    override val mIsParamsRequired: Boolean
        get() = false

}