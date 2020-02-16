package com.example.tinkoffnews.newscontent.gateway

import com.example.tinkoffnews.newscontent.api.NewsContentApi
import com.example.tinkoffnews.newscontent.domain.INewsContentGateway
import com.example.tinkoffnews.newscontent.domain.NewsContent
import com.example.tinkoffnews.newscontent.domain.NewsContentConverter
import io.reactivex.Single

class NewsContentGateway(
    private val newsContentApi: NewsContentApi,
    private val newsContentConverter: NewsContentConverter
) : INewsContentGateway {

    override fun getNewsContent(id: String): Single<NewsContent> =
        newsContentApi.getNewsContent(id)
            .map { response ->
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    newsContentConverter.fromNetwork(body)
                } else {
                    throw Exception("error code: ${response.code()}")
                }
            }
}