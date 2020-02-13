package com.example.tinkoffnews.newsdetails.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.tinkoffnews.R
import com.example.tinkoffnews.utils.viewModel
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news_content.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class NewsContentFragment : Fragment(R.layout.fragment_news_content), KodeinAware {

    override val kodein: Kodein by kodein()

    private val viewModel: NewsContentViewModel by viewModel {
        arguments?.getString(ARG_NEWS_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.newsContent
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(AndroidLifecycleScopeProvider.from(viewLifecycleOwner))
            .subscribe(
                {
                    newsContentTextView.text = it
                },
                {

                }
            )
    }

    companion object {
        const val ARG_NEWS_ID = "NewsId"
    }
}
