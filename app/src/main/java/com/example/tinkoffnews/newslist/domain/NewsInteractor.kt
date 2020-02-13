package com.example.tinkoffnews.newslist.domain

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.example.tinkoffnews.newslist.gateway.NewsGateway
import com.example.tinkoffnews.newslist.repository.NewsRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class NewsInteractor(
    private val newsRepository: INewsRepository,
    private val newsGateway: INewsGateway,
    private val newsBoundaryCallback: NewsBoundaryCallback
) {

    fun getNewsPagedList(): Flowable<PagedList<NewsBlock>> {

        val dataSource = newsRepository.getDataSource()

        val pagedListConfig = PagedList.Config.Builder()
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .setPageSize(LIST_SIZE)
            .setMaxSize(Int.MAX_VALUE)
            .setEnablePlaceholders(false)
            .build()

        return RxPagedListBuilder(dataSource, pagedListConfig)
            .setBoundaryCallback(newsBoundaryCallback)
            .setFetchScheduler(Schedulers.io())
            .buildFlowable(BackpressureStrategy.BUFFER)

    }

    fun refreshNews(): Completable =
        newsRepository
            .deleteAllNews()
            .andThen(newsGateway.getNews())
            .flatMapCompletable { newsRepository.saveNews(it) }

    companion object {

        const val LIST_SIZE = 20
        const val PREFETCH_DISTANCE = LIST_SIZE / 2
        const val INITIAL_LOAD_SIZE = LIST_SIZE * 2
    }
}