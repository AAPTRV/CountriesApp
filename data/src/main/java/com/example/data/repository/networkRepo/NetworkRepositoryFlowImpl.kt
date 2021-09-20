package com.example.data.repository.networkRepo

import com.example.data.ext.convertToDto
import com.example.data.network.FlowCountryService
import com.example.domain.dto.SingleCapitalDto
import com.example.domain.repository.network.NetworkRepositoryFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class NetworkRepositoryFlowImpl(private val mFlowService: FlowCountryService) :

    NetworkRepositoryFlow {

    override fun getCapitalsListFlow(): Flow<List<SingleCapitalDto>> =
        mFlowService.getCapitalsFlow().map { it.convertToDto() }.flowOn(Dispatchers.IO)

    override fun getCapitalsByNameFlow(name: String): Flow<List<SingleCapitalDto>> =
        mFlowService.getCapitalsByNameFlow(name).map { it.convertToDto() }.flowOn(Dispatchers.IO)

}