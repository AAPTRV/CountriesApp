package com.example.task1new.screens.countryList

import com.example.task1new.base.mvp.BaseMvpView
import com.example.task1new.dto.PostCountryItemDto

interface CountryListView : BaseMvpView {
    fun showDataInfo()

    fun addNewUniqueItemsInRecycleAdapter(data: List<PostCountryItemDto>)
}