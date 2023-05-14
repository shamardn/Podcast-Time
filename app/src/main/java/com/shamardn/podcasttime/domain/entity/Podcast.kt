package com.shamardn.podcasttime.domain.entity

data class Podcast(
    val feedUrl:String,
    val artworkUrl100:String,
    val releaseDate:String,
    val artistName: String,
    val collectionId: Int,
    val primaryGenreName: String,
    val trackCount: Int,
    val trackId: Int,
    val trackName: String,
    val trackViewUrl: String,
)