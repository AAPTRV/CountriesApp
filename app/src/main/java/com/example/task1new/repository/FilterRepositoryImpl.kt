package com.example.task1new.repository

import com.example.task1new.utils.filter.CountryDtoListFilterObject

class FilterRepositoryImpl : FilterRepository {

    override fun getFilter(): CountryDtoListFilterObject = CountryDtoListFilterObject

}