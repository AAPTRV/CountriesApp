package com.example.data.network

import com.example.data.model.CountryModel
import com.example.data.model.SingleCapitalModel
import com.example.data.utils.NetConstants
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryService {

    @GET(NetConstants.SERVER_API_POSTS_URL)
    fun getCountryList(): Flowable<List<CountryModel>>

    @GET(NetConstants.GET_COUNTRY_BY_NAME)
    fun getCountryByName(@Path(NetConstants.PATH_VARIABLE) name: String): Flowable<List<CountryModel>>

    @GET(NetConstants.GET_CAPITALS)
    fun getCapitals(): Flowable<SingleCapitalModel>

}