package com.example.tinkoffnews.app.di

import androidx.room.Room
import com.example.tinkoffnews.app.db.AppDatabase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object DatabaseInjectionModule {

    val module = Kodein.Module(DatabaseInjectionModule.javaClass.name) {

        bind<AppDatabase>() with singleton {

            Room
                .databaseBuilder( instance(), AppDatabase::class.java, AppDatabase.DATABASE_NAME )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}