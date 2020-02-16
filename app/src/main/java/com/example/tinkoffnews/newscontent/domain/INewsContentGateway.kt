package com.example.tinkoffnews.newscontent.domain

import io.reactivex.Single

interface INewsContentGateway {

    fun getNewsContent(id: String): Single<NewsContent>
}