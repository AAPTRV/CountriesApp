package com.example.task1new

import com.example.data.network.CoroutinesCountryService
import com.example.data.network.CountryService
import com.example.data.network.FlowCountryService
import com.example.data.utils.NetConstants
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

    private val coroutinesRetrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
        .baseUrl(NetConstants.SERVER_API_BASE_URL)
        .client(okHttpClient)
        .build()

    private val flowRetrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
        .baseUrl(NetConstants.SERVER_API_BASE_URL)
        .client(okHttpClient)
        .build()

    private val COUNTRY_SERVICE: CountryService = retrofitBuilder.create(CountryService::class.java)
    private val COROUTINES_COUNTRY_SERVICE: CoroutinesCountryService =
        coroutinesRetrofitBuilder.create(CoroutinesCountryService::class.java)
    private val FLOW_COUNTRY_SERVICE: FlowCountryService =
        flowRetrofitBuilder.create(FlowCountryService::class.java)

    fun getCountriesApi(): CountryService = COUNTRY_SERVICE
    fun getCountriesCoroutinesApi(): CoroutinesCountryService = COROUTINES_COUNTRY_SERVICE
    fun getCountriesFlowApi(): FlowCountryService = FLOW_COUNTRY_SERVICE

    init {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }

}