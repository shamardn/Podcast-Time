package com.shamardn.podcasttime.ui

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.shamardn.podcasttime.R

fun View.showSnakeBarError(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
        .setAction(this.context.resources.getString(R.string.ok)) {}
            .setActionTextColor(ContextCompat.getColor(this.context, R.color.white))
            .show()
}

fun View.showRetrySnakeBar(message: String, retry: () -> Unit) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(this.context.resources.getString(R.string.retry)) { retry.invoke() }
            .setActionTextColor(ContextCompat.getColor(this.context, R.color.white))
            .show()
}

