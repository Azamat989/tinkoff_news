package com.example.tinkoffnews.newsdetails.di

import com.example.tinkoffnews.newsdetails.ui.NewsContentViewModel
import com.example.tinkoffnews.newsdetails.api.NewsDetailsApi
import com.example.tinkoffnews.newsdetails.domain.NewsContentConverter
import com.example.tinkoffnews.newsdetails.domain.NewsContentInteractor
import com.example.tinkoffnews.newsdetails.gateway.NewsContentGateway
import org.kodein.di.Kodein
import org.kodein.di.generic.*
import retrofit2.Retrofit

object NewsContentInjectionModule {

    val module = Kodein.Module(NewsContentInjectionModule.javaClass.name) {

        bind<NewsDetailsApi>() with singleton {
            instance<Retrofit>().create(NewsDetailsApi::class.java)
        }

        bind<NewsContentConverter>()with singleton {
            NewsContentConverter()
        }

        bind<NewsContentGateway>() with singleton {
            NewsContentGateway(instance(), instance())
        }

        bind<NewsContentInteractor>() with singleton {
            NewsContentInteractor(instance())
        }

        bind<NewsContentViewModel>() with factory { newsId: String ->
            NewsContentViewModel(
                instance(),
                newsId
            )
        }
    }
}