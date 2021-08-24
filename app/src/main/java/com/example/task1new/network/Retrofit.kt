package com.example.task1new

import com.example.task1new.network.CoroutineCountryService
import com.example.task1new.network.CountryService
import com.example.task1new.utils.NetConstants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
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

    private val coroutineRetrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(NetConstants.SERVER_API_BASE_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
        .client(okHttpClient)
        .build()

    val COUNTRY_SERVICE: CountryService = retrofitBuilder.create(CountryService::class.java)

    val COROUTINE_COUNTRY_SERVICE: CoroutineCountryService = coroutineRetrofitBuilder.create(CoroutineCountryService::class.java)

    fun getCountriesApi(): CountryService = COUNTRY_SERVICE

    init {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
}