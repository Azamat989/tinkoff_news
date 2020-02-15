package com.example.tinkoffnews.newscontent.domain

import com.example.tinkoffnews.newscontent.api.NewsContentResponse

class NewsContentConverter {

    fun fromNetwork(newsContent: NewsContentResponse): NewsContent =
        NewsContent(newsContent.payload.content)

}