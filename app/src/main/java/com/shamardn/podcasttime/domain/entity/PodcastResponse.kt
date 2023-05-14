package com.shamardn.podcasttime.domain.entity

data class PodcastResponse(
    val resultCount: Int,
    val results: List<Podcast>
)