package com.example.tinkoffnews.newsdetails.gateway

import com.example.tinkoffnews.newsdetails.api.NewsDetailsApi
import com.example.tinkoffnews.newsdetails.domain.NewsContent
import com.example.tinkoffnews.newsdetails.domain.NewsContentConverter
import io.reactivex.Single

class NewsContentGateway(
    private val newsDetailsApi: NewsDetailsApi,
    private val newsContentConverter: NewsContentConverter
) {

    fun getNewsContent(id: String): Single<NewsContent> =
        newsDetailsApi.getNewsContent(id)
            .map { newsContentConverter.fromNetwork(it) }
}