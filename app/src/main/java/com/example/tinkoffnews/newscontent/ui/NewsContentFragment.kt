package com.example.tinkoffnews.newscontent.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
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

        setHasOptionsMenu(true)

        (requireActivity() as MainActivity).supportActionBar?.apply {
            title = viewModel.newsId
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

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

        private const val KEY_SCROLL_VIEW_STATE = "KEY_SCROLL_VIEW_STATE"
        const val TAG = "NewsContentFragment"
    }
}
