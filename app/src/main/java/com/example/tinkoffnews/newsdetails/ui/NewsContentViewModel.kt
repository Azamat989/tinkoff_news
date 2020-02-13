package com.example.tinkoffnews.newsdetails.ui

import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import com.example.tinkoffnews.newsdetails.domain.NewsContentInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class NewsContentViewModel(
    newsContentInteractor: NewsContentInteractor,
    newsId: String
) : ViewModel() {

    val newsContent = BehaviorSubject.create<Spanned>().toSerialized()

    private val compositeDisposable = CompositeDisposable()

    init {

        newsContentInteractor
            .getNewsContent(newsId)
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    companion object {
        const val TAG = "NewsDetailsViewModel"
    }
}
