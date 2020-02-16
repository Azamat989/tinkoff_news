package com.example.tinkoffnews.newscontent.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.navigation.fragment.navArgs
import com.example.tinkoffnews.R
import com.example.tinkoffnews.app.ui.MainActivity
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

    private val args: NewsContentFragmentArgs by navArgs()

    private val viewModel: NewsContentViewModel by viewModel {
        args.newsId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()

        retryButton.setOnClickListener {
            viewModel.getNewsContent()
        }

    }

    override fun onStart() {
        super.onStart()

        listenForDataChange()
    }

    override fun onSaveInstanceState(outState: Bundle) {

        val firstVisibleLineOffset =
            newsContentTextView.layout.getLineForVertical(contentScrollView.scrollY)

        val firstVisibleCharacterOffset =
            newsContentTextView.layout.getLineStart(firstVisibleLineOffset)

        outState.putInt(KEY_SCROLL_VIEW_STATE, firstVisibleCharacterOffset)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.getInt(KEY_SCROLL_VIEW_STATE)?.let {

            contentScrollView.postDelayed(
                {
                    val firstVisibleLineOffset = newsContentTextView.layout.getLineForOffset(it)
                    val pixelOffset = newsContentTextView.layout.getLineTop(firstVisibleLineOffset)
                    contentScrollView.scrollBy(0, pixelOffset)
                },
                10
            )
        }
    }

    private fun listenForDataChange() {

        viewModel
            .newsContent
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(AndroidLifecycleScopeProvider.from(viewLifecycleOwner, ON_STOP))
            .subscribe(
                {
                    newsContentTextView.text = it
                },
                {
                    Log.e(TAG, it.message ?: "No error message...")
                }
            )

        viewModel
            .isRefreshing
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(AndroidLifecycleScopeProvider.from(viewLifecycleOwner, ON_STOP))
            .subscribe(
                {
                    if (it) showProgress()
                    else hideProgress()
                },
                { Log.e(TAG, it.message ?: "No error message...") }
            )

        viewModel
            .shouldShowRetryButton
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(AndroidLifecycleScopeProvider.from(viewLifecycleOwner, ON_STOP))
            .subscribe(
                {
                    if (it) showRetryButton()
                    else hideRetryButton()
                },
                { Log.e(TAG, it.message ?: "No error message...") }
            )

    }

    private fun setupToolbar() {

        setHasOptionsMenu(true)

        (requireActivity() as MainActivity).supportActionBar?.apply {
            title = viewModel.newsId
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideRetryButton() {
        retryButton.visibility = View.INVISIBLE
    }

    private fun showRetryButton() {
        retryButton.visibility = View.VISIBLE
    }

    companion object {

        private const val KEY_SCROLL_VIEW_STATE = "KEY_SCROLL_VIEW_STATE"
        const val TAG = "NewsContentFragment"
    }
}
