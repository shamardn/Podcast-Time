package com.shamardn.podcasttime.data.datasource.remote.dtos

data class PodcastDTO(
    val artistId: Int,
    val artistName: String,
    val artistViewUrl: String,
    val artworkUrl600: String,
    val collectionId: Int,
    val collectionName: String,
    val collectionViewUrl: String,
    val country: String,
    val feedUrl: String,
    val genreIds: List<String>,
    val genres: List<String>,
    val kind: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val trackCount: Int,
    val trackId: Long,
    val trackName: String,
    val trackTimeMillis: Int,
    val trackViewUrl: String,
    val wrapperType: String
)