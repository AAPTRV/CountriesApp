package com.example.data.network

import com.example.data.model.CountryModel
import com.example.data.model.SingleCapitalModel
import com.example.data.utils.NetConstants
import retrofit2.http.GET
import retrofit2.http.Path

interface CoroutinesCountryService {

    @GET(NetConstants.SERVER_API_POSTS_URL)
    fun getCountryListCoroutines(): List<CountryModel>

    @GET(NetConstants.GET_COUNTRY_BY_NAME)
    fun getCountryByNameCoroutines(@Path(NetConstants.PATH_VARIABLE) name: String): List<CountryModel>

    @GET(NetConstants.GET_CAPITALS)
    fun getCapitalsCoroutines(): List<SingleCapitalModel>

}