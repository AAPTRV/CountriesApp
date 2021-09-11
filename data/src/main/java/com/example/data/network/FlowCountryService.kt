package com.example.data.network

import com.example.data.model.SingleCapitalModel
import com.example.data.utils.NetConstants
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface FlowCountryService {

    @GET(NetConstants.GET_CAPITALS)
    suspend fun getCapitalsFlow(): Flow<List<SingleCapitalModel>>

}