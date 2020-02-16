package com.example.tinkoffnews.app.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tinkoffnews.app.di.PreferencesInjectionModule
import com.example.tinkoffnews.newslist.domain.NewsInteractor
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Completable
import io.reactivex.Single

class MainActivityViewModel(
    private val rxSharedPreferences: RxSharedPreferences,
    private val newsInteractor: NewsInteractor
) : ViewModel() {

    fun loadDataIfFirstLaunch(): Completable =
        isFirstLaunch()
            .flatMapCompletable {
                Log.d(TAG, "isFirstLaunch=$it")

                if (it) {
                    newsInteractor
                        .refreshNews()
                        .andThen(setIsFirstLaunchFalse())
                } else {
                    Completable.complete()
                }
            }

    private fun isFirstLaunch(): Single<Boolean> =
        Single.fromCallable {
            rxSharedPreferences
                .getBoolean(PreferencesInjectionModule.PREF_IS_FIRST_LAUNCH, true)
                .get()
        }

    private fun setIsFirstLaunchFalse(): Completable =
        Completable.fromRunnable {
            rxSharedPreferences
                .getBoolean(PreferencesInjectionModule.PREF_IS_FIRST_LAUNCH)
                .set(false)
        }

    companion object {
        const val TAG = "MainActivityViewModel"

    }
}