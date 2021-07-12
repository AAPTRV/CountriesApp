package com.example.task1new.dto

data class PostCountryItemDto(
    val name: String,
    val capital: String,
    val population: Int,
    val languages: List<LanguagesDto>
)