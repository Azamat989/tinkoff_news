package com.example.tinkoffnews.newslist.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tinkoffnews.R
import com.example.tinkoffnews.newsdetails.ui.NewsContentFragment
import com.example.tinkoffnews.utils.viewModel
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class NewsFragment : Fragment(R.layout.fragment_news), KodeinAware {

    override val kodein: Kodein by kodein()

    private val viewModel: NewsViewModel by viewModel()

    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupViews()

        listenForNews()
    }

    private fun setupViews() {

        newsSwipeToRefreshLayout.setOnRefreshListener {
            viewModel.refreshNews()
        }

        newsAdapter = NewsAdapter { newsId ->

            Log.d(TAG, "newsId=$newsId")
            val fr = NewsContentFragment()

            val bundle = Bundle().apply { putString(NewsContentFragment.ARG_NEWS_ID, newsId) }

            fr.arguments = bundle

            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.fragmentContainer, fr)
            }
        }

        newsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        newsRecyclerView.adapter = newsAdapter
    }

    private fun listenForNews() {

        viewModel.news
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(AndroidLifecycleScopeProvider.from(viewLifecycleOwner))
            .subscribe(
                {
                    Log.d(TAG, "next PagedList: size=${it.size}")
                    newsAdapter.submitList(it)
                },
                {
                    Log.e(TAG, it.message ?: "No error message...")
                }
            )

        viewModel.isRefreshing
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(AndroidLifecycleScopeProvider.from(viewLifecycleOwner))
            .subscribe(
                {
                    newsSwipeToRefreshLayout.isRefreshing = it
                },
                {
                    Log.e(TAG, it.message ?: "No error message...")
                }
            )
    }



    companion object {
        const val TAG = "NewsFragment"
    }
}
