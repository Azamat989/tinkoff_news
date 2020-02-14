package com.example.tinkoffnews.app.di

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object PreferencesInjectionModule {

    const val PREF_IS_FIRST_LAUNCH = "PREF_IS_FIRST_LAUNCH"

    val module = Kodein.Module(PreferencesInjectionModule.javaClass.name) {

        bind<SharedPreferences>() with singleton {
            PreferenceManager.getDefaultSharedPreferences(instance())
        }

        bind<RxSharedPreferences>() with singleton {
            RxSharedPreferences.create(instance())
        }
    }
}