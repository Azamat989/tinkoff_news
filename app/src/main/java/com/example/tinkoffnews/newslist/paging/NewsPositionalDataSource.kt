package com.example.tinkoffnews.newslist.paging

import android.util.Log
import androidx.paging.PositionalDataSource
import com.example.tinkoffnews.newslist.domain.INewsRepository
import com.example.tinkoffnews.newslist.domain.NewsBlock

class NewsPositionalDataSource(
    private val newsRepository: INewsRepository
) : PositionalDataSource<NewsBlock>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<NewsBlock>) {
        Log.d(
            TAG,
            "loadRange, startPosition = ${params.startPosition} loadSize = ${params.loadSize}"
        )

        val result = newsRepository.getNews(params.startPosition, params.loadSize)
        callback.onResult(result)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<NewsBlock>) {
        Log.d(
            TAG, "loadInitial, requestedStartPosition = ${params.requestedStartPosition} " +
                    "requestedLoadSize = ${params.requestedLoadSize}"
        )

        val result = newsRepository.getNews(params.requestedStartPosition, params.requestedLoadSize)
        callback.onResult(result, params.requestedStartPosition)
    }

    companion object {
        const val TAG = "PositionalDataSource"
    }
}