package com.example.task1new

import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceHolderApi {
    @GET(NetConstants.SERVER_API_POSTS_URL)
    fun getPosts(): Call<List<Post>>
}