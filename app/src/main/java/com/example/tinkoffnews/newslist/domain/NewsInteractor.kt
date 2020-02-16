package com.example.tinkoffnews.newslist.domain

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.example.tinkoffnews.newslist.domain.paging.NewsSourceFactory
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class NewsInteractor(
    private val newsRepository: INewsRepository,
    private val newsGateway: INewsGateway,
    private val newsSourceFactory: NewsSourceFactory
) {

    fun getNewsPagedList(): Flowable<PagedList<NewsBlock>> {

        val pagedListConfig = PagedList.Config.Builder()
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .setPageSize(LIST_SIZE)
            .setMaxSize(Int.MAX_VALUE)
            .setEnablePlaceholders(false)
            .build()

        return RxPagedListBuilder(newsSourceFactory, pagedListConfig)
            .setFetchScheduler(Schedulers.io())
            .buildFlowable(BackpressureStrategy.BUFFER)

    }

    fun refreshNews(): Completable =
        newsGateway
            .getNews()
            .flatMapCompletable {
                newsRepository.saveNews(it)
            }

    fun isDatabaseEmpty(): Flowable<Boolean> =
        newsRepository.isDatabaseEmpty()

    companion object {

        const val LIST_SIZE = 20
        const val PREFETCH_DISTANCE = LIST_SIZE / 2
        const val INITIAL_LOAD_SIZE = LIST_SIZE * 2
    }
}