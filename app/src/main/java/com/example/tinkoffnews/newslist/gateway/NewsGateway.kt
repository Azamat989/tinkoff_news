package com.example.tinkoffnews.newslist.gateway

import com.example.tinkoffnews.newslist.api.NewsApi
import com.example.tinkoffnews.newslist.domain.INewsGateway
import com.example.tinkoffnews.newslist.domain.NewsBlock
import com.example.tinkoffnews.newslist.domain.NewsConverter
import io.reactivex.Single

class NewsGateway(
    private val newsApi: NewsApi,
    private val newsConverter: NewsConverter
) : INewsGateway {

    override fun getNews(): Single<List<NewsBlock>> =
        newsApi
            .getNews()
            .map { newsResponse -> newsConverter.fromNetwork(newsResponse) }
            .onErrorReturn { listOf() }
}