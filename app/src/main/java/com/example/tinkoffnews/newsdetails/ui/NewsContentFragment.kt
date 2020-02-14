package com.example.tinkoffnews.newsdetails.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
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

    private val viewModel: NewsContentViewModel by viewModel {
        arguments?.getString(ARG_NEWS_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setHasOptionsMenu(true)

        (requireActivity() as MainActivity).supportActionBar?.title = viewModel.newsId

        setupViews()
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
            Log.e(TAG, "onViewStateRestored() scrollY=$it")

            contentScrollView.postDelayed(
                {
                    val firstVisibleLineOffset = newsContentTextView.layout.getLineForOffset(it)
                    val pixelOffset = newsContentTextView.layout.getLineTop(firstVisibleLineOffset)
                    contentScrollView.scrollBy(0, pixelOffset)
                },
                100
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =

        when (item.itemId) {
            R.id.action_back -> {
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun setupViews() {

        viewModel
            .newsContent
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(AndroidLifecycleScopeProvider.from(viewLifecycleOwner))
            .subscribe(
                {
                    hideProgress()
                    newsContentTextView.text = it
                },
                {
                    hideProgress()
                    Log.e(TAG, it.message ?: "No error message...")
                }
            )

    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    companion object {

        const val ARG_NEWS_ID = "NewsId"
        private const val KEY_SCROLL_VIEW_STATE = "KEY_SCROLL_VIEW_STATE"
        const val TAG = "NewsContentFragment"
    }
}
