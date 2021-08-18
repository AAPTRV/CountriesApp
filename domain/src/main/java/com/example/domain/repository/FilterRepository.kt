package com.example.domain.repository

import com.example.domain.filter.CountryDtoListFilterObject

interface FilterRepository {

    fun getFilter(): CountryDtoListFilterObject

}