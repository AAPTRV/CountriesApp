package com.example.data.repository.filterRepo

import com.example.domain.filter.CountryDtoListFilterObject
import com.example.domain.repository.filter.FilterRepository

class FilterRepositoryImpl : FilterRepository {
    // TODO: Make Filter Non-static (through creating new filters)
    override fun getFilter(): CountryDtoListFilterObject = CountryDtoListFilterObject

}