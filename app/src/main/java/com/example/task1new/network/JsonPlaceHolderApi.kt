package com.example.task1new.network

import com.example.task1new.model.PostCountryItemModel
import com.example.task1new.utils.NetConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceHolderApi {

    @GET(NetConstants.SERVER_API_POSTS_URL)
    fun getPosts(): Call<List<PostCountryItemModel>>

    @GET(NetConstants.GET_COUNTRY_BY_NAME)
    fun getCountryByName(@Path(NetConstants.PATH_VARIABLE) name: String): Call<List<PostCountryItemModel>>

}