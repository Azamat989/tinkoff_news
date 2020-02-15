package com.example.tinkoffnews.newscontent.ui

import android.os.Handler
import android.os.Looper
import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import com.example.tinkoffnews.newscontent.domain.NewsContentInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class NewsContentViewModel(
    private val newsContentInteractor: NewsContentInteractor,
    val newsId: String
) : ViewModel() {

    val newsContent = BehaviorSubject.create<Spanned>().toSerialized()

    val isRefreshing = BehaviorProcessor.create<Boolean>().toSerialized()

    val shouldShowRetryButton = BehaviorProcessor.create<Boolean>().toSerialized()

    private val handler = Handler(Looper.getMainLooper())

    private val compositeDisposable = CompositeDisposable()

    init { getNewsContent() }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getNewsContent() {

        newsContentInteractor
            .getNewsContent(newsId)
            .doOnSubscribe {
                isRefreshing.onNext(true)
                shouldShowRetryButton.onNext(false)
            }
            .doOnError {
                handler.postDelayed(
                    {
                        isRefreshing.onNext(false)
                        shouldShowRetryButton.onNext(true)
                    },
                    300
                )
            }
            .doOnSuccess {
                isRefreshing.onNext(false)
                shouldShowRetryButton.onNext(false)
            }
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    val content = HtmlCompat.fromHtml(it.content, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    newsContent.onNext(content)
                },
                { Log.e(TAG, it.message ?: "No error message...") }
            )
            .let { compositeDisposable.add(it) }
    }

    companion object {
        const val TAG = "NewsContentViewModel"
    }
}
