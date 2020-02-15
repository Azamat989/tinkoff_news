package com.example.tinkoffnews.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tinkoffnews.newslist.db.NewsBlockEntity
import com.example.tinkoffnews.newslist.db.NewsDao

@Database(entities = [NewsBlockEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}