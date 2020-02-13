package com.example.tinkoffnews.newslist.api

import com.google.gson.annotations.SerializedName

class NewsResponse(
    @SerializedName("payload") val list: List<NewsBlockResponse>
)

class NewsBlockResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("text") val text: String,
    @SerializedName("publicationDate") val publicationDate: PublicationDate,
    @SerializedName("bankInfoTypeId") val bankInfoTypeId: Int
)

class PublicationDate(
    @SerializedName("milliseconds") val milliseconds: Long
)
