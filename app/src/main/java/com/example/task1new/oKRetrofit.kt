package com.example.task1new

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object OkRetrofit {

    private val loggingInterceptor = HttpLoggingInterceptor()
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(NetConstants.SESSION_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(NetConstants.SESSION_TIMEOUT, TimeUnit.MILLISECONDS)
        .addInterceptor(loggingInterceptor)
        .build()
    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(NetConstants.SERVER_API_BASE_URL)
        .client(okHttpClient)
        .build()
    private val jsonPlaceHolderApi = retrofitBuilder.create(JsonPlaceHolderApi::class.java)
    val retrofitData = jsonPlaceHolderApi.getPosts()

    init {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
}