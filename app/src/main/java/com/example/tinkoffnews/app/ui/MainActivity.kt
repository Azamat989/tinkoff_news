package com.example.tinkoffnews.app.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tinkoffnews.R
import com.example.tinkoffnews.utils.viewModel
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by kodein()

    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        startApp()
    }

    private fun startApp() {

        viewModel
            .loadDataIfFirstLaunch()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle))
            .subscribe(
                { Log.d(TAG, "app started") },
                { Log.e(TAG, it.message ?: "No error message...") }
            )
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
