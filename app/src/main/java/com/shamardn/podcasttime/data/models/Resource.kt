package com.shamardn.podcasttime.data.models

sealed class Resource<out T>(
    val data: T? = null, val exception: Exception? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    object Loading : Resource<Nothing>()
    class Error<T>(message: Exception, data: T? = null) : Resource<T>(data, message)
}