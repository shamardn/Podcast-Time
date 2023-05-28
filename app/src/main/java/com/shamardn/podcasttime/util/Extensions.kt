package com.shamardn.podcasttime.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun Long.formatDate(pattern: String): String{
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    val date = Date(this * 1000)
    return simpleDateFormat.format(date)
}

// Call requires API level 26
@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeStamp(timestamp: Long) {
    val rightFormatter = DateTimeFormatter
        .ofPattern(
            "dd MM yyyy",
            Locale.getDefault()
        )
        .format(Instant.ofEpochMilli(timestamp))
}

fun Int.milliSecondsToMinutes(): String {
    val minutes: Int = (this / 60000) % 60
    val hours: Int = this / 3600000
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