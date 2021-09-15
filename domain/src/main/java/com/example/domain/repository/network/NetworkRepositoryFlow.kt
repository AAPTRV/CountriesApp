package com.example.domain.repository.network

import com.example.domain.dto.SingleCapitalDto
import kotlinx.coroutines.flow.Flow

interface NetworkRepositoryFlow {

    fun getCapitalsListFlow(): Flow<List<SingleCapitalDto>>

    fun getCapitalsByNameFlow(name: String): Flow<List<SingleCapitalDto>>

}