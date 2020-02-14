package com.example.tinkoffnews.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.example.tinkoffnews.R
import com.example.tinkoffnews.newslist.ui.NewsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.news_toolbar))

        if(supportFragmentManager.fragments.size == 0) {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, NewsFragment())
            }
        }
    }
}
