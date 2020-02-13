package com.example.tinkoffnews.newslist.domain

import com.example.tinkoffnews.newslist.api.NewsBlockResponse
import com.example.tinkoffnews.newslist.api.NewsResponse
import com.example.tinkoffnews.newslist.db.NewsBlockEntity

class NewsConverter {

    fun fromNetwork(newsResponse: NewsResponse): List<NewsBlock> =
        newsResponse.list.map { newsBlock ->
            fromNetwork(newsBlock)
        }

    fun fromDatabase(newsBlocks: List<NewsBlockEntity>): List<NewsBlock> =
        newsBlocks.map { newsBlockEntity ->
            fromDatabase(newsBlockEntity)
        }

    fun toDatabase(newsBlocks: List<NewsBlock>): List<NewsBlockEntity> =
        newsBlocks.map { newsBlock ->
            toDatabase(newsBlock)
        }

    private fun fromNetwork(newsBlock: NewsBlockResponse): NewsBlock =
        NewsBlock(
            id = newsBlock.id,
            name = newsBlock.name,
            text = newsBlock.text,
            publicationDate = newsBlock.publicationDate.milliseconds
        )

    private fun fromDatabase(newsBlock: NewsBlockEntity): NewsBlock =
        NewsBlock(
            id = newsBlock.id,
            name = newsBlock.name,
            text = newsBlock.text,
            publicationDate = newsBlock.publicationDate
        )

    private fun toDatabase(newsBlock: NewsBlock): NewsBlockEntity =
        NewsBlockEntity(
            id = newsBlock.id,
            name = newsBlock.name,
            text = newsBlock.text,
            publicationDate = newsBlock.publicationDate
        )
}