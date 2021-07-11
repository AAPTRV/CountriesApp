package com.example.task1new.dto

import com.example.task1new.model.Languages

class CountryDto (
    val name: String,
    val capital: String,
    val population: Int,
    val languages: List<Languages>
)