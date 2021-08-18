package com.example.task1new.model

import com.example.domain.dto.LanguageDto
import com.example.domain.dto.LatLngDto
import com.example.domain.dto.CountryDto
import com.google.gson.annotations.SerializedName

fun List<CountryModel>.convertToCountryDto(): List<CountryDto> {
    val resultListDto = mutableListOf<CountryDto>()
    for (model in this) {
        resultListDto.add(model.convertToCountryItemDto())
    }
    return resultListDto
}

data class CountryModel(
    val name: String?,
    val capital: String?,
    val population: Int?,
    val languages: List<LanguageModel>,
    @SerializedName("flag")
    val flag: String?,
    val latlng: List<Double?>,
    val area: Double?
) {

    fun convertToCountryItemDto(): CountryDto {

        var mDtoName = "No name"
        var mDtoCapital = "No capital"
        var mDtoPopulation = 0
        val mDtoLanguages: MutableList<LanguageDto> = mutableListOf()
        var mDtoFlagUrl = ""
        var mDtoArea = 0.0
        var mDtoLocation = mutableListOf<Double>()

        this.name?.let {
            if (it.isNotEmpty()) {
                mDtoName = it
            }
        }
        this.capital?.let {
            if (it.isNotEmpty()) {
                mDtoCapital = it
            }
        }
        this.population?.let {
            mDtoPopulation = it
        }
        this.flag?.let {
            mDtoFlagUrl = it
        }

        this.area?.let {
            mDtoArea = it
        }
        this.languages.forEach { mDtoLanguages.add(it.convertToDto()) }
        for(item in this.latlng){
            item?.let{mDtoLocation.add(item)}
        }
        return CountryDto(mDtoName, mDtoCapital, mDtoPopulation, mDtoLanguages, mDtoFlagUrl, mDtoArea, mDtoLocation)
    }

    fun convertToLatLngDto(): LatLngDto {
        var mName = "No name"
        var mLatitude = 0.0
        var mLongitude = 0.0
        this.latlng[0]?.let {
            mLatitude = it
        }
        this.latlng[1]?.let {
            mLongitude = it
        }
        this.name?.let{
            mName = it
        }
        return LatLngDto(
            mName, mLatitude, mLongitude
        )
    }
}




