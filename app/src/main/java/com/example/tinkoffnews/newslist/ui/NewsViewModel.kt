package com.example.tinkoffnews.newslist.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.tinkoffnews.newslist.domain.NewsBlock
import com.example.tinkoffnews.newslist.domain.NewsInteractor
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class NewsViewModel(
    private val newsInteractor: NewsInteractor
) : ViewModel() {

    val news = BehaviorProcessor.create<PagedList<NewsBlock>>().toSerialized()

    val isRefreshing = BehaviorProcessor.create<Boolean>().toSerialized()

    var recyclerViewState: Bundle? = null

    private val compositeDisposable = CompositeDisposable()

    init {

        newsInteractor
            .getNewsPagedList()
            .doOnSubscribe { isRefreshing.onNext(true) }
            .doOnNext { isRefreshing.onNext(false) }
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Log.d(TAG, "next PagedList: size=${it.size}")
                    news.onNext(it)
                },
                { Log.e(TAG, it.message ?: "No error message...") }
            )
            .let { compositeDisposable.add(it) }

        newsInteractor
            .isDatabaseEmpty()
            .onBackpressureLatest()
            .flatMapCompletable {
                Log.d(TAG, "isDatabaseEmpty=$it")
                invalidateCurrentDataSource()
            }
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Log.d(TAG, "invalidateCurrentDataSource() is called") },
                { Log.e(TAG, it.message ?: "No error message...") }
            )
            .let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun refreshNews() {
        newsInteractor
            .refreshNews()
            .doOnSubscribe { isRefreshing.onNext(true) }
            .doFinally { isRefreshing.onNext(false) }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = { Log.e(TAG, it.message ?: "No error message...") }
            )
            .let { compositeDisposable.add(it) }
    }

    private fun invalidateCurrentDataSource(): Completable =
        news.firstElement()
            .flatMapCompletable {
                Completable
                    .fromRunnable { it.dataSource.invalidate() }
            }

    companion object {
        const val TAG = "NewsViewModel"
    }
}
