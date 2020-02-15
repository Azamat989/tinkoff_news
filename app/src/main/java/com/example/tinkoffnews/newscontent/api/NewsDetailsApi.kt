package com.example.tinkoffnews.newscontent.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsDetailsApi {

    @GET("news_content")
    fun getNewsContent(
        @Query("id") id: String
    ) : Single<NewsContentResponse>
}