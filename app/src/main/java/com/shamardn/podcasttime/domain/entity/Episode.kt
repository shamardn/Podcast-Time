package com.shamardn.podcasttime.domain.entity

data class Episode(
    val artistIds: List<Any>,
    val artistName: String,
    val artworkUrl100: String,
    val artworkUrl160: String,
    val artworkUrl30: String,
    val artworkUrl60: String,
    val artworkUrl600: String,
    val closedCaptioning: String,
    val collectionCensoredName: String,
    val collectionExplicitness: String,
    val collectionHdPrice: Int,
    val collectionId: Int,
    val collectionName: String,
    val collectionPrice: Double,
    val collectionViewUrl: String,
    val contentAdvisoryRating: String,
    val country: String,
    val currency: String,
    val description: String,
    val episodeContentType: String,
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
    val trackCensoredName: String,
    val trackCount: Int,
    val trackExplicitness: String,
    val trackId: Long,
    val trackName: String,
    val trackPrice: Double,
    val trackTimeMillis: Int,
    val trackViewUrl: String,
    val wrapperType: String
)