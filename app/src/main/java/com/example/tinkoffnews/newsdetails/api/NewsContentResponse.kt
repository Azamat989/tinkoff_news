package com.example.tinkoffnews.newsdetails.api

import com.google.gson.annotations.SerializedName

class NewsContentResponse(
    @SerializedName("payload") val payload: NewsPayload
)

class NewsPayload(
    @SerializedName("content") val content: String
)
