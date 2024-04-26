package com.shamardn.podcasttime

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.schedulers.Schedulers

@HiltAndroidApp
class PodcastTimeApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        checkFirstTimeLaunch()
        listenToNetworkConnectivity()
    }
    private fun checkFirstTimeLaunch() {

    }

    @SuppressLint("CheckResult")
    fun listenToNetworkConnectivity() {
        ReactiveNetwork.observeInternetConnectivity().subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io()).subscribe { connected: Boolean ->
                isConnected = connected
                FirebaseCrashlytics.getInstance().setCustomKey("connected_to_internet", isConnected)
            }
    }

    companion object {
        private const val TAG = "MyApplication"
        var isConnected = false
        var isFirstTimeLaunch = false
    }
}