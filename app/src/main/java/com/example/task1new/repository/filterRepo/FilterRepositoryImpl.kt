package com.example.task1new.repository.filterRepo

import com.example.domain.filter.CountryDtoListFilterObject

class FilterRepositoryImpl : com.example.domain.repository.FilterRepository {
    // TODO: Make Filter Non-static (through creating new filters)
    override fun getFilter(): CountryDtoListFilterObject = CountryDtoListFilterObject

}