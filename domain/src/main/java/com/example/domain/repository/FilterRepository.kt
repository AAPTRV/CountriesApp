package com.example.domain.repository

import com.example.domain.dto.CountryFilterDto
import io.reactivex.rxjava3.subjects.BehaviorSubject

interface FilterRepository {

    fun getFilterSubject(): BehaviorSubject<CountryFilterDto>

    fun processNewQuery(query: String)

    fun precessNewArea(area:Float)

}