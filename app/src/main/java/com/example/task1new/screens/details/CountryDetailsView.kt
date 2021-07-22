package com.example.task1new.screens.details

import com.example.task1new.base.mvp.BaseMvpView
import com.example.task1new.dto.PostCountryItemDto
import com.google.android.gms.maps.model.LatLng

interface CountryDetailsView : BaseMvpView {

    fun showCountryInfo(country: PostCountryItemDto, location: LatLng)

}