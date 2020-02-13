package com.example.tinkoffnews.app

import android.app.Application
import com.example.tinkoffnews.app.di.AppInjectionModule
import com.facebook.stetho.Stetho
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {

    override val kodein = ConfigurableKodein()

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {

        kodein.apply {

            mutable = true

            clear()

            addImport(AppInjectionModule.module)

            addImport(Kodein.Module(javaClass.name) {
                bind<Application>() with singleton { this@App }
            })
        }
    }
}