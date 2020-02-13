package com.example.tinkoffnews.newslist.domain

data class NewsBlock(
    val id: String,
    val name: String,
    val text: String,
    val publicationDate: Long
)