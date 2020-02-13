package com.example.tinkoffnews.newsdetails.domain

import com.example.tinkoffnews.newsdetails.api.NewsContentResponse

class NewsContentConverter {

    fun fromNetwork(newsContent: NewsContentResponse): NewsContent =
        NewsContent(newsContent.payload.content)

}