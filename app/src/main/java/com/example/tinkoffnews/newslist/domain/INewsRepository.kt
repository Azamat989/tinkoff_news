package com.example.tinkoffnews.newslist.domain

import io.reactivex.Completable

interface INewsRepository {

    fun saveNews(news: List<NewsBlock>): Completable

    fun deleteAllNews(): Completable

    fun getNews(startPosition: Int, loadSize: Int): List<NewsBlock>
}