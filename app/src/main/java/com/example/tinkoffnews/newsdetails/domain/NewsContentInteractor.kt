package com.example.tinkoffnews.newsdetails.domain

import com.example.tinkoffnews.newsdetails.gateway.NewsContentGateway
import io.reactivex.Single

class NewsContentInteractor(
    private val newsContentGateway: NewsContentGateway
) {

    fun getNewsContent(id: String): Single<NewsContent> =
        newsContentGateway.getNewsContent(id)
}