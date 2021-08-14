package com.example.task1new.repository

import com.example.task1new.utils.filter.CountryDtoListFilterObject

interface FilterRepository {

    fun getFilter(): CountryDtoListFilterObject

}