package com.shamardn.podcasttime.services

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.shamardn.podcasttime.util.Constants.ACTION_PAUSE_SERVICE
import com.shamardn.podcasttime.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.shamardn.podcasttime.util.Constants.ACTION_STOP_SERVICE

class PodcastService: LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    Log.i("PodcastService","Started or resumed service")
                }
                ACTION_PAUSE_SERVICE -> {
                    Log.i("PodcastService", "Paused service")
                }
                ACTION_STOP_SERVICE -> {
                    Log.i("PodcastService", "Stopped service")
                }
                else -> {
                    Log.i("PodcastService", "No service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

}