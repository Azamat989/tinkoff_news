package com.example.tinkoffnews.newslist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<NewsBlockEntity>): Completable

    @Query("SELECT * FROM newsBlock LIMIT :loadSize OFFSET :startPosition")
    fun select(startPosition: Int, loadSize: Int): List<NewsBlockEntity>

    @Query("SELECT count(*) FROM newsBlock")
    fun countNews(): Flowable<Int>
}