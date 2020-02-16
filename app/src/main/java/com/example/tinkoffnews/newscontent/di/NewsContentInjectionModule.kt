package com.example.tinkoffnews.newscontent.di

import com.example.tinkoffnews.newscontent.api.NewsContentApi
import com.example.tinkoffnews.newscontent.domain.INewsContentGateway
import com.example.tinkoffnews.newscontent.domain.NewsContentConverter
import com.example.tinkoffnews.newscontent.domain.NewsContentInteractor
import com.example.tinkoffnews.newscontent.gateway.NewsContentGateway
import com.example.tinkoffnews.newscontent.ui.NewsContentViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.multiton
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

object NewsContentInjectionModule {

    val module = Kodein.Module(NewsContentInjectionModule.javaClass.name) {

        bind<NewsContentApi>() with singleton {
            instance<Retrofit>().create(NewsContentApi::class.java)
        }

        bind<NewsContentConverter>() with singleton {
            NewsContentConverter()
        }

        bind<INewsContentGateway>() with singleton {
            NewsContentGateway(instance(), instance())
        }

        bind<NewsContentInteractor>() with singleton {
            NewsContentInteractor(instance())
        }

        bind<NewsContentViewModel>() with multiton { newsId: String ->
            NewsContentViewModel(
                instance(),
                newsId
            )
        }
    }
}