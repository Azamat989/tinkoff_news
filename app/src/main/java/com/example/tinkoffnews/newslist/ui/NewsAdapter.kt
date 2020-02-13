package com.example.tinkoffnews.newslist.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tinkoffnews.R
import com.example.tinkoffnews.newslist.domain.NewsBlock
import kotlinx.android.synthetic.main.item_news_block.view.*

class NewsAdapter(
    private val onItemClickedListener: (String) -> Unit
) : PagedListAdapter<NewsBlock, NewsAdapter.NewsBlockViewHolder>(diffUtilItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsBlockViewHolder {

        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_news_block, parent, false)

        return NewsBlockViewHolder(view, onItemClickedListener)
    }

    override fun onBindViewHolder(holder: NewsBlockViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }


    class NewsBlockViewHolder(
        private val view: View,
        private val onItemClickedListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        fun bind(newsBlock: NewsBlock) {
            view.newsTextView.text = newsBlock.text

            Log.d(TAG, newsBlock.publicationDate.toString())

            view.setOnClickListener {
                Log.d(TAG, "newsId=${newsBlock.id}")
                onItemClickedListener.invoke(newsBlock.id)
            }
        }
    }

    companion object {

        const val TAG = "NewsAdapter"

        private val diffUtilItemCallback = object : DiffUtil.ItemCallback<NewsBlock>() {
            override fun areItemsTheSame(oldItem: NewsBlock, newItem: NewsBlock): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NewsBlock, newItem: NewsBlock): Boolean =
                oldItem == newItem
        }
    }
}
