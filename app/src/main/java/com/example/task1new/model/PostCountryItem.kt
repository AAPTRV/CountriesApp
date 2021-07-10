package com.example.task1new.model

import com.example.task1new.model.Languages

data class PostCountryItem (
    val name: String,
    val capital: String,
    val population: Int,
    val languages: List<Languages>
)