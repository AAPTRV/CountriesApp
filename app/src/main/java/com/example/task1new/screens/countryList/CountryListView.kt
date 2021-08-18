package com.example.task1new.screens.countryList

import com.example.task1new.base.mvp.BaseMvpView
import com.example.domain.dto.CountryDto

interface CountryListView : BaseMvpView {

    fun addNewUniqueItemsInRecycleAdapter(data: List<CountryDto>)

    fun repopulateFilteredDataListInAdapter(data: List<CountryDto>)
}