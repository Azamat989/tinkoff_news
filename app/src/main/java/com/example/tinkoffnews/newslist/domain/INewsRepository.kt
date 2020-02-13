package com.example.tinkoffnews.newslist.domain

import androidx.paging.DataSource
import io.reactivex.Completable

interface INewsRepository {

    fun saveNews(news: List<NewsBlock>): Completable

    fun deleteAllNews(): Completable

    fun getDataSource(): DataSource.Factory<Int, NewsBlock>
}