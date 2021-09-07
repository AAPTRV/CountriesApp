package com.example.data.network

import com.example.data.model.SingleCapitalModel
import com.example.data.utils.NetConstants
import retrofit2.http.GET

interface CoroutinesCountryService {

    @GET(NetConstants.GET_CAPITALS)
    suspend fun getCapitalsCoroutines(): List<SingleCapitalModel>

}