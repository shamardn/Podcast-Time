package com.shamardn.podcasttime.ui.history.uistate

data class PodcastUiState(
    val trackId: Long = 0,
    val trackName: String = "",
    val artworkUrl100: String = "",
    val releaseDate: String = "",
    val trackCount: Int = 0,
    val primaryGenreName: String = "",
    val artistName: String = "",
)
