package com.example.tinkoffnews.newslist.di

import com.example.tinkoffnews.app.db.AppDatabase
import com.example.tinkoffnews.newslist.api.NewsApi
import com.example.tinkoffnews.newslist.db.NewsDao
import com.example.tinkoffnews.newslist.domain.*
import com.example.tinkoffnews.newslist.gateway.NewsGateway
import com.example.tinkoffnews.newslist.paging.NewsSourceFactory
import com.example.tinkoffnews.newslist.repository.NewsRepository
import com.example.tinkoffnews.newslist.ui.NewsViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.*
import retrofit2.Retrofit

object NewsListInjectionModule {

    val module = Kodein.Module(NewsListInjectionModule.javaClass.name) {

        bind<NewsApi>() with singleton {
            instance<Retrofit>()
                .create(NewsApi::class.java)
        }

        bind<NewsDao>() with singleton {
            instance<AppDatabase>().newsDao()
        }

        bind<NewsConverter>() with singleton {
            NewsConverter()
        }

        bind<INewsGateway>() with singleton {
            NewsGateway(instance(), instance())
        }

        bind<INewsRepository>() with singleton {
            NewsRepository(instance(), instance())
        }

        bind<NewsSourceFactory>() with singleton {
            NewsSourceFactory(instance())
        }

        bind<NewsInteractor>() with singleton {
            NewsInteractor(instance(), instance(), instance())
        }

        bind<NewsViewModel>() with singleton {
            NewsViewModel(instance(), instance())
        }
    }
}