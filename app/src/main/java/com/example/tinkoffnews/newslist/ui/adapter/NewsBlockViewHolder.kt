package com.example.tinkoffnews.newslist.ui.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tinkoffnews.newslist.domain.NewsBlock
import kotlinx.android.synthetic.main.item_news_block.view.*

class NewsBlockViewHolder(
    private val view: View,
    private val onItemClickedListener: (String) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(newsBlock: NewsBlock) {
        view.newsTextView.text = newsBlock.text

        view.setOnClickListener {
            Log.d(TAG, "newsId=${newsBlock.id}")
            onItemClickedListener.invoke(newsBlock.id)
        }
    }

    companion object {
        const val TAG = "NewsBlockViewHolder"
    }
}