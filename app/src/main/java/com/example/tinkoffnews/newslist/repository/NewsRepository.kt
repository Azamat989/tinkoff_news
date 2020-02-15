package com.example.tinkoffnews.newslist.repository

import com.example.tinkoffnews.newslist.db.NewsDao
import com.example.tinkoffnews.newslist.domain.INewsRepository
import com.example.tinkoffnews.newslist.domain.NewsBlock
import com.example.tinkoffnews.newslist.domain.NewsConverter
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class NewsRepository(
    private val newsDao: NewsDao,
    private val newsConverter: NewsConverter
) : INewsRepository {

    override fun saveNews(news: List<NewsBlock>): Completable =
        Single
            .just(newsConverter.toDatabase(news))
            .flatMapCompletable { entities -> newsDao.insert(entities) }

    override fun getNews(startPosition: Int, loadSize: Int): List<NewsBlock> =
        newsDao
            .select(startPosition, loadSize)
            .map { newsConverter.fromDatabase(it) }

    override fun isDatabaseEmpty(): Flowable<Boolean> =
        newsDao.countNews()
            .map { it == 0 }

}