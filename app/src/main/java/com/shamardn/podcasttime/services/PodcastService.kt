package com.shamardn.podcasttime.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.ui.main.MainActivity
import com.shamardn.podcasttime.util.Constants.ACTION_PAUSE_SERVICE
import com.shamardn.podcasttime.util.Constants.ACTION_SHOW_EPISODE_DETAILS_FRAGMENT
import com.shamardn.podcasttime.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.shamardn.podcasttime.util.Constants.ACTION_STOP_SERVICE
import com.shamardn.podcasttime.util.Constants.NOTIFICATION_CHANNEL_ID
import com.shamardn.podcasttime.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.shamardn.podcasttime.util.Constants.NOTIFICATION_ID

class PodcastService: LifecycleService() {

    var isFirstRun = true
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }else{
                        Log.i("PodcastService","Resuming Service ....")
                    }
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

    private fun startForegroundService(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
        as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_mic)
            .setContentTitle("Podcast Time Notification")
            .setContentText("text of our notification written here")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_EPISODE_DETAILS_FRAGMENT
        },
        FLAG_IMMUTABLE
    )
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
            )
        notificationManager.createNotificationChannel(channel)
    }
}