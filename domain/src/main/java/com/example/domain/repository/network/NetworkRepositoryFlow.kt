package com.example.domain.repository.network

import com.example.domain.dto.SingleCapitalDto
import kotlinx.coroutines.flow.Flow

interface NetworkRepositoryFlow {

    suspend fun getCapitalsListCoroutines(): Flow<List<SingleCapitalDto>>

}