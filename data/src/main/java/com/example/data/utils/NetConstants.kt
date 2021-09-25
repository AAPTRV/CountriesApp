package com.example.data.utils

object NetConstants {

    const val SESSION_TIMEOUT: Long = 10000

    const val PATH_VARIABLE = "path"

    const val PATH_URL = "/{$PATH_VARIABLE}"

    const val SERVER_API_BASE_URL = "https://restcountries.com/v2/"

    const val SERVER_API_POSTS_URL = "all"

    const val GET_COUNTRY_BY_NAME = "name$PATH_URL"

    const val GET_CAPITALS = "all?fields=capital"

    const val SEARCH_DELAY_MILLIS: Long = 500

    const val MIN_QUERY_LENGTH = 3

    const val GET_CAPITAL_BY_NAME = "capital$PATH_URL"

}