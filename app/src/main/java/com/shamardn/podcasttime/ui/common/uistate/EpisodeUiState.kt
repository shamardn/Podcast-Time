package com.shamardn.podcasttime.ui.common.uistate

data class EpisodeUiState(
    val id : Long,
    val collectionName: String,
    val collectionId: Int,
    val artistName: String,
    val trackName: String,
    val releaseDate: String,
    val description: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val episodeUrl: String,
    val guid: String,
    val episodeFileExtension: String,
)
