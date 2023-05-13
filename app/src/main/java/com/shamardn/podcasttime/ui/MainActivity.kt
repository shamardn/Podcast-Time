package com.shamardn.podcasttime.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.shamardn.podcasttime.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.PodcastTimeTheme)

        setContentView(R.layout.activity_main)

        installSplashScreen()
    }
}