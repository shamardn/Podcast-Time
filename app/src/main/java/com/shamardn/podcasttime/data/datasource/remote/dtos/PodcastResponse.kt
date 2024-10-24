package com.shamardn.podcasttime.data.datasource.remote.dtos

data class PodcastResponse<T>(
    val resultCount: Int,
    val results: List<T>
)