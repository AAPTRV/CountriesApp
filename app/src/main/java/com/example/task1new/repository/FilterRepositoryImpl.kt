package com.example.task1new.repository

import com.example.task1new.utils.filter.CountryDtoListFilterObject

class FilterRepositoryImpl : FilterRepository {
    // TODO: Make Filter Non-static (through creating new filters)
    override fun getFilter(): CountryDtoListFilterObject = CountryDtoListFilterObject

}