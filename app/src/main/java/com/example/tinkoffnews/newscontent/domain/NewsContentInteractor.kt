package com.example.tinkoffnews.newscontent.domain

import io.reactivex.Single

class NewsContentInteractor(
    private val newsContentGateway: INewsContentGateway
) {

    fun getNewsContent(id: String): Single<NewsContent> =
        newsContentGateway.getNewsContent(id)
}