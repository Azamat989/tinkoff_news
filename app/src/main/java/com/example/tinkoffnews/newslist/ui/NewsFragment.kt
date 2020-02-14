package com.example.tinkoffnews.newslist.ui

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tinkoffnews.R
import com.example.tinkoffnews.app.ui.MainActivity
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

        (requireActivity() as MainActivity).supportActionBar?.title =
            getString(R.string.news_list_title)

        setupAdapter()

        setupViews()
    }

    override fun onStart() {
        super.onStart()

        listenForDataChange()
    }

    override fun onStop() {
        super.onStop()

        saveRecyclerViewState()
    }

    private fun setupViews() {

        newsSwipeToRefreshLayout.setOnRefreshListener {
            viewModel.refreshNews()
        }

        newsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        newsRecyclerView.adapter = newsAdapter

    }

    private fun setupAdapter() {

        newsAdapter = NewsAdapter { newsId ->
            Log.d(TAG, "newsId=$newsId")
            openNewsContentFragment(newsId)
        }

        newsAdapter.registerAdapterDataObserver(getAdapterDataObserver())
    }

    private fun openNewsContentFragment(newsId: String) {

        val contentFragment = NewsContentFragment()

        val bundle =
            Bundle().apply {
                putString(NewsContentFragment.ARG_NEWS_ID, newsId)
            }

        contentFragment.arguments = bundle

        parentFragmentManager.commit {
            addToBackStack(NewsContentFragment::class.java.name)
            replace(R.id.fragmentContainer, contentFragment)
        }
    }

    private fun listenForDataChange() {

        viewModel.news
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle, Lifecycle.Event.ON_STOP))
            .subscribe(
                {
                    Log.d(TAG, "next PagedList: size=${it.size}")
                    newsAdapter.submitList(it)
                },
                { Log.e(TAG, it.message ?: "No error message...") }
            )

        viewModel.isRefreshing
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle, Lifecycle.Event.ON_STOP))
            .subscribe(
                { newsSwipeToRefreshLayout.isRefreshing = it },
                { Log.e(TAG, it.message ?: "No error message...") }
            )
    }

    private fun getAdapterDataObserver(): RecyclerView.AdapterDataObserver =

        object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                restoreRecyclerViewState()
            }
        }

    private fun saveRecyclerViewState() {

        viewModel.recyclerViewState = Bundle()

        newsRecyclerView
            .layoutManager
            ?.onSaveInstanceState()
            ?.let {
                Log.d(TAG, "parcelable=$it")
                viewModel.recyclerViewState?.putParcelable(KEY_RECYCLER_STATE, it)
            }
    }

    private fun restoreRecyclerViewState() {

        viewModel
            .recyclerViewState
            ?.getParcelable<Parcelable>(KEY_RECYCLER_STATE)
            ?.let {
                newsRecyclerView.layoutManager?.onRestoreInstanceState(it)
            }
    }

    companion object {

        const val TAG = "NewsFragment"

        private const val KEY_RECYCLER_STATE = "KEY_RECYCLER_STATE"
    }
}
