package com.example.task1new.screens.details

import com.example.task1new.base.mvp.BaseMvpView
import com.example.domain.dto.CountryDto
import com.google.android.gms.maps.model.LatLng

interface CountryDetailsView : BaseMvpView{
    fun showCountryInfo(country: CountryDto, location: LatLng)
}