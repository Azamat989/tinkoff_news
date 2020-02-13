package com.example.tinkoffnews.newslist.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "newsBlock")
data class NewsBlockEntity(
    @PrimaryKey val id: String,
    val name: String,
    val text: String,
    val publicationDate: Long
)