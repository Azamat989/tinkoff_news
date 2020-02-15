package com.example.tinkoffnews.newslist.domain

import io.reactivex.Completable
import io.reactivex.Flowable

interface INewsRepository {

    fun saveNews(news: List<NewsBlock>): Completable

    fun getNews(startPosition: Int, loadSize: Int): List<NewsBlock>

    fun isDatabaseEmpty(): Flowable<Boolean>
}