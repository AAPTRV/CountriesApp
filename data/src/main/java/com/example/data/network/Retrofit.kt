package com.example.data.network

import com.example.data.NetConstants
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Retrofit {

    private val loggingInterceptor = HttpLoggingInterceptor()
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(NetConstants.SESSION_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(NetConstants.SESSION_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(NetConstants.SESSION_TIMEOUT, TimeUnit.MILLISECONDS)
        .addInterceptor(loggingInterceptor)
        .build()
    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(NetConstants.SERVER_API_BASE_URL)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
    val COUNTRY_SERVICE: CountryService = retrofitBuilder.create(CountryService::class.java)

    fun getCountriesApi(): CountryService = COUNTRY_SERVICE

    init {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
}