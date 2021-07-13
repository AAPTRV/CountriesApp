package com.example.task1new.model

import com.example.task1new.dto.LanguageDto
import com.example.task1new.dto.PostCountryItemDto
import java.lang.StringBuilder

fun List<PostCountryItemModel>.convertToDto(): List<PostCountryItemDto>{
    val resultListDto = mutableListOf<PostCountryItemDto>()
    for (model in this){
        resultListDto.add(model.convertToDto())
    }
    return resultListDto
}

data class PostCountryItemModel(
    val name: String?,
    val capital: String?,
    val population: Int?,
    val languages: List<LanguageModel>
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

    fun convertToDto(): PostCountryItemDto{

        var mDtoName = "No name"
        var mDtoCapital = "No capital"
        var mDtoPopulation = 0
        val mDtoLanguages: MutableList<LanguageDto> = mutableListOf()

        if(!this.name.isNullOrEmpty()){
            mDtoName = this.name
        }
        if(!this.capital.isNullOrEmpty()){
            mDtoCapital = this.capital
        }
        if(this.population != null){
            mDtoPopulation = this.population
        }
        for(Language in this.languages){
            mDtoLanguages.add(Language.convertToDto())
        }

        return PostCountryItemDto(mDtoName,mDtoCapital,mDtoPopulation,mDtoLanguages.convertListToString())
    }
}




