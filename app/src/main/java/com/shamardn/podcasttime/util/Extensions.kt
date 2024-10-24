package com.shamardn.podcasttime.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.Locale

fun String.isEmailValid(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Long.milliSecondsToMinutes(): String {
    val minutes = (this / 60000).toInt() % 60 + 1
    val hours = (this / 3600000).toInt()
    return when {
        minutes == 0 -> "$hours hr"
        hours == 0 -> "$minutes min"
        else -> "$hours hr $minutes min"
    }
}

fun String.changeDateFormat(): String? {
    val currentFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    val desiredFormat = "dd MMM yyyy"
    val currentDateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
    val desiredDateFormat = SimpleDateFormat(desiredFormat, Locale.getDefault())
    val date = currentDateFormat.parse(this)
    return date?.let { desiredDateFormat.format(it) }
}

fun <T> MutableLiveData<Event<T>>.postEvent(content: T) {
    postValue(Event(content))
}

inline fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline onEventUnhandledContent: (T) -> Unit
) {
    observe(owner) { it?.getContentIfNotHandled()?.let(onEventUnhandledContent) }
}