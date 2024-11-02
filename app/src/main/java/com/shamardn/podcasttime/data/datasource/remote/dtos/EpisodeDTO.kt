package com.shamardn.podcasttime.data.datasource.remote.dtos

data class EpisodeDTO(
    val artistIds: List<Any>,
    val artistName: String,
    val artworkUrl600: String,
    val collectionId: Int,
    val collectionName: String,
    val country: String,
    val description: String,
    val episodeFileExtension: String,
    val episodeGuid: String,
    val episodeUrl: String,
    val feedUrl: String,
    val genreIds: List<String>,
    val genres: List<Any>,
    val kind: String,
    val previewUrl: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val shortDescription: String,
    val trackCount: Int,
    val trackId: Long,
    val trackName: String,
    val trackTimeMillis: Long,
    val trackViewUrl: String,
    val wrapperType: String
)