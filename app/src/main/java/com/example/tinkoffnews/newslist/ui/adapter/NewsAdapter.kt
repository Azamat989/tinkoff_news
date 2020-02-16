package com.example.tinkoffnews.newslist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.tinkoffnews.R
import com.example.tinkoffnews.newslist.domain.NewsBlock

class NewsAdapter(
    private val onItemClickedListener: (String) -> Unit
) : PagedListAdapter<NewsBlock, NewsBlockViewHolder>(diffUtilItemCallback) {

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
