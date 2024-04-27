package com.shamardn.podcasttime.util

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsUtils {

    /**
     * Endpoint Keys
     */
    const val CUSTOM_ENDPOINT_KEY = "CUSTOM_ENDPOINT_KEY"

    /**
     * random cases key
     */
    const val CUSTOM_KEY = "CUSTOM_KEY"
    const val SEARCH_KEY = "SEARCH_KEY"


    fun sendLogToCrashlytics(msg: String, vararg keys: String) {
        keys.forEach { key ->
            FirebaseCrashlytics.getInstance().setCustomKey(key, msg)
        }
        FirebaseCrashlytics.getInstance().recordException(CustomCrashlyticsLogException(msg))
    }

    fun sendLogToCrashlytics(msg: String, vararg keys: Pair<String, String>) {
        keys.forEach { key ->
            FirebaseCrashlytics.getInstance().setCustomKey(key.first, key.second)
        }
        FirebaseCrashlytics.getInstance().recordException(CustomCrashlyticsLogException(msg))
    }

    // generic method to avoid create a log in one crash with a new variant and create a new separate crashlytics log
    inline fun <reified T : Exception> sendCustomLogToCrashlytics(
        msg: String, vararg keys: Pair<String, String>
    ) {
        keys.forEach { key ->
            FirebaseCrashlytics.getInstance().setCustomKey(key.first, key.second)
        }

        val exception = T::class.java.getConstructor(String::class.java).newInstance(msg)
        FirebaseCrashlytics.getInstance().recordException(exception)
    }

}

class CustomCrashlyticsLogException(message: String) : Exception(message)
class SearchException(message: String) : Exception(message)