package com.example.tinkoffnews.newslist.domain.paging

import androidx.paging.DataSource
import com.example.tinkoffnews.newslist.domain.INewsRepository
import com.example.tinkoffnews.newslist.domain.NewsBlock

class NewsSourceFactory(
    private val newsRepository: INewsRepository
) : DataSource.Factory<Int, NewsBlock>() {

    override fun create(): DataSource<Int, NewsBlock> =
        NewsPositionalDataSource(newsRepository)
}