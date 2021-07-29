package com.example.task1new.screens.map

import com.example.task1new.base.mvp.BaseMvpView
import com.example.task1new.dto.LatLngDto
import com.example.task1new.dto.PostCountryItemDto
import com.example.task1new.model.PostCountryItemModel

interface MapsView: BaseMvpView {

    fun showAllCountryMarkersOnMap(dtoList: List<LatLngDto>){
    }

}