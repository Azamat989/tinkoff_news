package com.example.tinkoffnews.newslist.domain

import io.reactivex.Single

interface INewsGateway {

    fun getNews(): Single<List<NewsBlock>>

}