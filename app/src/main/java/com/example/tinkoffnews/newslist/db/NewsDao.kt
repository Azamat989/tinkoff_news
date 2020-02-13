package com.example.tinkoffnews.newslist.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<NewsBlockEntity>): Completable

    @Query("SELECT * FROM newsBlock ORDER BY publicationDate DESC")
    fun select(): DataSource.Factory<Int, NewsBlockEntity>

    @Query("DELETE FROM newsBlock")
    fun deleteAll(): Completable
}