package com.example.domain.repository.filter

import com.example.domain.filter.CountryDtoListFilterObject

interface FilterRepository {

    fun getFilter(): CountryDtoListFilterObject

}