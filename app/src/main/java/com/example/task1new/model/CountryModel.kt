package com.example.task1new.model

import com.example.task1new.dto.FlagDto
import com.example.task1new.dto.LanguageDto
import com.example.task1new.dto.LatLngDto
import com.example.task1new.dto.PostCountryItemDto
import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder

fun List<PostCountryItemModel>.convertToCountryDto(): List<PostCountryItemDto> {
    val resultListDto = mutableListOf<PostCountryItemDto>()
    for (model in this) {
        resultListDto.add(model.convertToPostCountryItemDto())
    }
    return resultListDto
}

data class PostCountryItemModel(
    val name: String?,
    val capital: String?,
    val population: Int?,
    val languages: List<LanguageModel>,
    @SerializedName("flag")
    val flag: String?,
    val latlng: List<Double?>,
    val area: Double?
) {

    fun convertToPostCountryItemDto(): PostCountryItemDto {

        var mDtoName = "No name"
        var mDtoCapital = "No capital"
        var mDtoPopulation = 0
        val mDtoLanguages: MutableList<LanguageDto> = mutableListOf()
        var mDtoFlagUrl = ""
        var mDtoArea = 0.0

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
        return PostCountryItemDto(mDtoName, mDtoCapital, mDtoPopulation, mDtoLanguages, mDtoFlagUrl, mDtoArea)
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




