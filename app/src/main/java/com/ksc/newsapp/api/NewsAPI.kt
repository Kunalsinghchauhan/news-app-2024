package com.ksc.newsapp.api

import com.ksc.newsapp.models.NewsResponse
import com.ksc.newsapp.utils.Constant.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale.IsoCountryCode

interface NewsAPI {
    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode: String ="in",
        @Query("pageNumber")
        pageNumber :Int = 1,
        @Query("apiKey")
        apiKey :String=API_KEY
    ) :Response<NewsResponse>

    @GET("/v2/everything")
    suspend fun getSearchEverything(
        @Query("q")
        searchQuery: String ,
        @Query("pageNumber")
        pageNumber :Int = 1,
        @Query("apiKey")
        apiKey :String=API_KEY
    ) :Response<NewsResponse>
}