package com.example.tinkoffnews.newscontent.domain

import com.example.tinkoffnews.newscontent.gateway.NewsContentGateway
import io.reactivex.Single

class NewsContentInteractor(
    private val newsContentGateway: NewsContentGateway
) {

    fun getNewsContent(id: String): Single<NewsContent> =
        newsContentGateway.getNewsContent(id)
}