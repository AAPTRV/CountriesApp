package com.repository.filter

import com.example.task1new.base.filter.CountryDtoListFilterObject
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.subjects.BehaviorSubject

class FilterRepositoryImpl : FilterRepository {

    private val mFilterSubject = BehaviorSubject.createDefault(CountryFilter())

    override fun getFilterSubject(): BehaviorSubject<CountryFilter> = mFilterSubject

    override fun processNewQuery(query: String) {
        mFilterSubject.value.name = query
        mFilterSubject.onNext(mFilterSubject.value)
    }

    override fun precessNewArea(area: Float) {
        mFilterSubject.value.area = area
        mFilterSubject.onNext(mFilterSubject.value)
    }

}