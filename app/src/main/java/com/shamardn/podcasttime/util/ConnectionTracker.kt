package com.shamardn.podcasttime.util

interface ConnectionTracker {
    suspend fun isInternetConnectionAvailable(): Boolean
}