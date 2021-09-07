package com.example.domain.repository.network

import com.example.domain.dto.SingleCapitalDto

interface NetworkRepositoryCoroutines {

    suspend fun getCapitalsListCoroutines(): List<SingleCapitalDto>

}