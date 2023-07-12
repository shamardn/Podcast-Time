package com.shamardn.podcasttime

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PodcastTimeApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        checkFirstTimeLaunch()
    }
    private fun checkFirstTimeLaunch() {

    }
    companion object {
        var isFirstTimeLaunch = false
    }
}