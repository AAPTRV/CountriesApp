package com.example.data.model

import android.util.Log
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

fun CountryDto.convertToLatLngDto(): LatLngDto {
    return LatLngDto(
        this.name, this.location[0], this.location[1]
    )
}

fun List<CountryDto>.convertToLatLngDto(): List<LatLngDto> {
    val resultList = mutableListOf<LatLngDto>()
    for (dto in this) {
        resultList.add(dto.convertToLatLngDto())
    }
    return resultList
}

data class CountryModel(
    val name: String?,
    val capital: String?,
    val population: Int?,
    val languages: List<LanguageModel?>?,
    @SerializedName("flags")
    val flag: List<String?>?,
    val latlng: List<Double?>?,
    val area: Double?
) {

    fun convertToCountryItemDto(): CountryDto {

        var mDtoName = "No name"
        var mDtoCapital = "No capital"
        var mDtoPopulation = 0
        val mDtoLanguages: MutableList<LanguageDto> = mutableListOf()
        var mDtoFlagUrl = ""
        var mDtoArea = 0.0
        val mDtoLocation = mutableListOf<Double>()

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

        this.flag?.let { flags ->
            if (flags.isNotEmpty()) {
                flags[0]?.let { mDtoFlagUrl = it }
            }
        }

        this.area?.let {
            mDtoArea = it
        }

        this.languages?.let { languages ->
            if (languages.isNotEmpty()) {
                for (language in languages) {
                    language?.let { mDtoLanguages.add(it.convertToDto()) }
                }
            }
        }

        this.latlng?.let { latlng ->
            for (item in latlng) {
                item?.let { mDtoLocation.add(it) }
            }
        }

        return CountryDto(
            mDtoName,
            mDtoCapital,
            mDtoPopulation,
            mDtoLanguages,
            mDtoFlagUrl,
            mDtoArea,
            mDtoLocation
        )
    }

    fun convertToLatLngDto(): LatLngDto {
        var mName = "No name"
        var mLatitude = 0.0
        var mLongitude = 0.0
        this.latlng?.let { latlng ->
            latlng[0]?.let { mLatitude = it }
            latlng[1]?.let { mLongitude = it }
        }
        this.name?.let {
            mName = it
        }
        return LatLngDto(
            mName, mLatitude, mLongitude
        )
    }
}




