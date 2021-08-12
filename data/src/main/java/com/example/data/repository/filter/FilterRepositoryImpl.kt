package com.example.data.repository.filter

import com.example.domain.dto.CountryFilterDto
import io.reactivex.rxjava3.subjects.BehaviorSubject

class FilterRepositoryImpl : com.example.domain.repository.FilterRepository {

    private val mFilterSubject = BehaviorSubject.createDefault(CountryFilterDto())

    override fun getFilterSubject(): BehaviorSubject<CountryFilterDto> = mFilterSubject

    override fun processNewQuery(query: String) {
        mFilterSubject.value.name = query
        mFilterSubject.onNext(mFilterSubject.value)
    }

    override fun precessNewArea(area: Float) {
        mFilterSubject.value.area = area
        mFilterSubject.onNext(mFilterSubject.value)
    }

}