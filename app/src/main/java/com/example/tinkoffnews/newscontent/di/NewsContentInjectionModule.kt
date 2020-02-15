package com.example.tinkoffnews.newscontent.di

import com.example.tinkoffnews.newscontent.ui.NewsContentViewModel
import com.example.tinkoffnews.newscontent.api.NewsDetailsApi
import com.example.tinkoffnews.newscontent.domain.NewsContentConverter
import com.example.tinkoffnews.newscontent.domain.NewsContentInteractor
import com.example.tinkoffnews.newscontent.gateway.NewsContentGateway
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

        bind<NewsContentViewModel>() with multiton { newsId: String ->
            NewsContentViewModel(
                instance(),
                newsId
            )
        }
    }
}