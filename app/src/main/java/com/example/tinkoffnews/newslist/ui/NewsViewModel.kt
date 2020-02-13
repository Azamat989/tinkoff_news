package com.example.tinkoffnews.newslist.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.tinkoffnews.newslist.domain.NewsBlock
import com.example.tinkoffnews.newslist.domain.NewsInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class NewsViewModel(
    private val newsInteractor: NewsInteractor
) : ViewModel() {

    val news = BehaviorProcessor.create<PagedList<NewsBlock>>().toSerialized()

    val isRefreshing = BehaviorProcessor.create<Boolean>().toSerialized()

    private val compositeDisposable = CompositeDisposable()

    init {

        getNews()
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "onCleared() is called")
        compositeDisposable.clear()
    }

    private fun getNews() {
        newsInteractor
            .getNewsPagedList()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Log.d(TAG, "next PagedList: size=${it.size}")
                    news.onNext(it)
                },
                {
                    Log.e(TAG, it.message ?: "No error message...")
                }
            )
            .let { compositeDisposable.add(it) }
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

    companion object {
        const val TAG = "NewsViewModel"
    }
}