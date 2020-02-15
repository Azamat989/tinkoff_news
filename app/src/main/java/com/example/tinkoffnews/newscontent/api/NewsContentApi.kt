package com.example.tinkoffnews.newscontent.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsContentApi {

    @GET("news_content")
    fun getNewsContent(
        @Query("id") id: String
    ) : Single<Response<NewsContentResponse>>
}