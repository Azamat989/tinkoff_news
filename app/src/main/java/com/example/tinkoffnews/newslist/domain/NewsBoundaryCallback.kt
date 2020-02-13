package com.example.tinkoffnews.newslist.domain

import android.util.Log
import androidx.paging.PagedList
import com.example.tinkoffnews.newslist.gateway.NewsGateway
import com.example.tinkoffnews.newslist.repository.NewsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewsBoundaryCallback(
    private val newsGateway: INewsGateway,
    private val newsRepository: INewsRepository
) : PagedList.BoundaryCallback<NewsBlock>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onZeroItemsLoaded() {

        newsGateway
            .getNews()
            .flatMapCompletable { newsRepository.saveNews(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.d(TAG, "SUCCESS") },
                { Log.e(TAG, it.message ?: "No error message...") }
            )
            .let { compositeDisposable.add(it) }
    }

    companion object {
        const val TAG = "NewsBoundaryCallback"
    }
}