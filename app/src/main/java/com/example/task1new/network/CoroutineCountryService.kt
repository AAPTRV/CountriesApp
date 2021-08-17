package com.example.task1new.network

import com.example.task1new.model.CountryModel
import com.example.task1new.utils.NetConstants
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface CoroutineCountryService {

    @GET(NetConstants.SERVER_API_POSTS_URL)
    suspend fun getCountryList(): List<CountryModel>

    @GET(NetConstants.GET_COUNTRY_BY_NAME)
    suspend fun getCountryByName(@Path(NetConstants.PATH_VARIABLE) name: String): List<CountryModel>

}