package com.example.tinkoffnews.newscontent.gateway

import com.example.tinkoffnews.newscontent.api.NewsDetailsApi
import com.example.tinkoffnews.newscontent.domain.NewsContent
import com.example.tinkoffnews.newscontent.domain.NewsContentConverter
import io.reactivex.Single

class NewsContentGateway(
    private val newsDetailsApi: NewsDetailsApi,
    private val newsContentConverter: NewsContentConverter
) {

    fun getNewsContent(id: String): Single<NewsContent> =
        newsDetailsApi.getNewsContent(id)
            .map { newsContentConverter.fromNetwork(it) }
}