package com.example.data.network

import com.example.data.model.SingleCapitalModel
import com.example.data.utils.NetConstants
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface FlowCountryService {

    @GET(NetConstants.GET_CAPITALS)
    fun getCapitalsFlow(): Flow<List<SingleCapitalModel>>

    @GET(NetConstants.GET_CAPITAL_BY_NAME)
    fun getCapitalsByNameFlow(@Path(NetConstants.PATH_VARIABLE) name: String): Flow<List<SingleCapitalModel>>

}