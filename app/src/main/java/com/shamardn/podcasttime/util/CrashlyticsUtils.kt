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
    const val NOTIFICATION_KEY = "NOTIFICATION_KEY"
    const val LOGIN_KEY = "LOGIN_KEY"
    const val DETAILS_KEY = "DETAILS_KEY"
    const val DOWNLOADS_KEY = "DOWNLOADS_KEY"
    const val SUBSCRIPTIONS_KEY = "SUBSCRIPTIONS_KEY"
    const val REGISTER_KEY = "REGISTER_KEY"
    const val FORGET_PASSWORD_KEY = "FORGET_PASSWORD_KEY"
    const val AUTH_PROVIDER = "AUTH_PROVIDER"
    const val PLAYLIST_KEY = "PLAYLIST_KEY"
    const val LISTEN_TO_USER_DETAILS = "LISTEN_TO_USER_DETAILS"


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
class NotificationException(message: String) : Exception(message)
class LoginException(message: String) : Exception(message)
class RegisterException(message: String) : Exception(message)
class ForgetPasswordException(message: String) : Exception(message)
class UserDetailsException(message: String) : Exception(message)
class DetailsException(message: String) : Exception(message)
class DownloadsException(message: String) : Exception(message)
class SubscriptionException(message: String) : Exception(message)
class PlaylistException(message: String) : Exception(message)
