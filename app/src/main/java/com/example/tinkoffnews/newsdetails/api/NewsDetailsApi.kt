package com.example.tinkoffnews.newsdetails.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsDetailsApi {

    @GET("news_content")
    fun getNewsContent(
        @Query("id") id: String
    ) : Single<NewsContentResponse>
}