package com.repository.filter

import com.example.task1new.base.filter.CountryDtoListFilterObject
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.BehaviorSubject

interface FilterRepository {

    fun getFilterSubject(): BehaviorSubject<CountryFilter>

    fun processNewQuery(query: String)

    fun precessNewArea(area:Float)

}