package com.example.domain.usecase.impl

import com.example.domain.dto.RoomCommonInfoDto
import com.example.domain.repository.database.DatabaseCommonInfoRepository
import com.example.domain.usecase.UseCase
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable

class GetCountriesCommonInfoUseCase(private val mDatabaseCommonInfoRepository: DatabaseCommonInfoRepository) :
    UseCase<Unit, List<RoomCommonInfoDto>>() {
    override fun buildFlowable(params: Unit?): Flowable<List<RoomCommonInfoDto>> = Flowable.create({
        val result = mDatabaseCommonInfoRepository.getAllInfo()
        it.onNext(result)
        it.onComplete()
    }, BackpressureStrategy.LATEST)

    override val mIsParamsRequired: Boolean
        get() = false

}