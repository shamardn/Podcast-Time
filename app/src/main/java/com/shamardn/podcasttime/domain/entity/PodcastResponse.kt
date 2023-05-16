package com.shamardn.podcasttime.domain.entity

data class PodcastResponse<T>(
    val resultCount: Int,
    val results: List<T>
)