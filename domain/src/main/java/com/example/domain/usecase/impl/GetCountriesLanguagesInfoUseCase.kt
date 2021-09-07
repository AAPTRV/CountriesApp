package com.example.domain.usecase.impl

import com.example.domain.dto.RoomLanguageDto
import com.example.domain.repository.database.DatabaseLanguageRepository
import com.example.domain.usecase.UseCase
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable

class GetCountriesLanguagesInfoUseCase(private val mDatabaseLanguagesInfoRepository: DatabaseLanguageRepository) :
    UseCase<Unit, List<RoomLanguageDto>>() {
    override fun buildFlowable(params: Unit?): Flowable<List<RoomLanguageDto>> = Flowable.create({
        val result = mDatabaseLanguagesInfoRepository.getAllInfo()
        it.onNext(result)
        it.onComplete()
    }, BackpressureStrategy.LATEST)

    override val mIsParamsRequired: Boolean
        get() = false

}