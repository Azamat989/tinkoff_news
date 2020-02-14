package com.example.tinkoffnews.app.di

import com.example.tinkoffnews.newsdetails.di.NewsContentInjectionModule
import com.example.tinkoffnews.newslist.di.NewsListInjectionModule
import org.kodein.di.Kodein

object AppInjectionModule {

    val module = Kodein.Module(AppInjectionModule.javaClass.name) {

        import(ApiInjectionModule.module)

        import(DatabaseInjectionModule.module)

        import(PreferencesInjectionModule.module)

        import(NewsListInjectionModule.module)

        import(NewsContentInjectionModule.module)
    }
}