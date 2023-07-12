package com.shamardn.podcasttime.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

class ConnectionTrackerImpl @Inject constructor(
    context: Context,
) : ConnectionTracker {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun isInternetConnectionAvailable(): Boolean {
        return if (isConnectedToWifiOrCellular()) {
            checkInternetAvailability()
        } else false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isConnectedToWifiOrCellular(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private suspend fun checkInternetAvailability(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val socket = Socket()
                val socketAddress = InetSocketAddress(HOST_NAME, PORT)
                socket.connect(socketAddress, CONNECTION_TIMEOUT)
                socket.close()
            }
            true
        } catch (e: Exception) {
            Log.d("ConnectionTrackerImpl", "ERROR: $e")
            false
        }
    }

    companion object {
        private const val HOST_NAME = "8.8.8.8"
        private const val PORT = 53
        private const val CONNECTION_TIMEOUT = 3000
    }
}