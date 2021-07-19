package com.example.task1new.model

import com.example.task1new.dto.FlagDto
import com.example.task1new.dto.LanguageDto
import com.example.task1new.dto.LatLngDto
import com.example.task1new.dto.PostCountryItemDto
import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder

fun List<PostCountryItemModel>.convertToPostCountryItemDto(): List<PostCountryItemDto> {
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
    val flagImageUrl: String?,
    val latlng: List<Double>
) {
    private fun MutableList<LanguageDto>.convertListToString(): String {
        val sb = StringBuilder()
        this.forEachIndexed { index, item ->
            if (this.size > 1 && index != this.size - 1) {
                sb.append(item)
                sb.append(",")
            } else {
                sb.append(item)
            }
        }
        return sb.toString()
    }

    fun convertToPostCountryItemDto(): PostCountryItemDto {

        var mDtoName = "No name"
        var mDtoCapital = "No capital"
        var mDtoPopulation = 0
        val mDtoLanguages: MutableList<LanguageDto> = mutableListOf()

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
        this.languages.forEach { mDtoLanguages.add(it.convertToDto()) }
        return PostCountryItemDto(mDtoName, mDtoCapital, mDtoPopulation, mDtoLanguages)
    }

    fun convertToFlagDto(): FlagDto {
        var mFlagImageUrl =
            "https://yt3.ggpht.com/ytc/AAUvwnj-HQCcnej-gvi_dCwAz8TmulUOPoHLGlS05rMi=s900-c-k-c0x00ffffff-no-rj"
        this.flagImageUrl?.let {
            if (it.isNotEmpty()) {
                mFlagImageUrl = it
            }
        }
        return FlagDto(mFlagImageUrl)
    }

    fun convertToLatLngDto(): LatLngDto {
        return LatLngDto(
            this.latlng[0], this.latlng[1]
        )
    }
}




