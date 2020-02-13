package com.example.tinkoffnews.newslist.api

import io.reactivex.Single
import retrofit2.http.GET

interface NewsApi {

    @GET("news")
    fun getNews(): Single<NewsResponse>
}