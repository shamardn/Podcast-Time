package com.shamardn.podcasttime.ui.common.uistate

data class PodcastUiState(
    val trackId: Long,
    val artistName: String,
    val collectionName: String,
    val artworkUrl600: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val trackCount: Int,
    val trackName: String,
)
