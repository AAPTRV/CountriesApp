package com.example.task1new.screens.map

import com.example.task1new.base.mvp.BaseMvpView
import com.example.task1new.dto.LatLngDto

interface MapsView: BaseMvpView {

    fun showAllCountryMarkersOnMap(dtoList: List<LatLngDto>){
    }

}