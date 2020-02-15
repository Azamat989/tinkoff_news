package com.example.tinkoffnews.app.di

import com.example.tinkoffnews.app.ui.MainActivityViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object MainActivityInjectionModule {

    val module = Kodein.Module(MainActivityInjectionModule.javaClass.name) {

        bind<MainActivityViewModel>() with singleton {
            MainActivityViewModel(instance(), instance())
        }
    }
}