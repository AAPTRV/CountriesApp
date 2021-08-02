package com.example.task1new.network

import com.example.task1new.model.CountryModel
import com.example.task1new.utils.NetConstants
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceHolderApi {

    @GET(NetConstants.SERVER_API_POSTS_URL)
    fun getPosts(): Flowable<List<CountryModel>>

    @GET(NetConstants.GET_COUNTRY_BY_NAME)
    fun getCountryByName(@Path(NetConstants.PATH_VARIABLE) name: String): Flowable<List<CountryModel>>

}