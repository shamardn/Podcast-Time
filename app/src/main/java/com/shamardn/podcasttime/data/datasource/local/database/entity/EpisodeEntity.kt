package com.shamardn.podcasttime.data.datasource.local.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "EPISODE_TABLE")
data class EpisodeEntity(
    @PrimaryKey()
    val guid: String,
    val id : Long,
    val collectionId: Int,
    val collectionName: String,
    val artistName: String,
    val trackName: String,
    val releaseDate: String,
    val description: String,
    val trackTimeMillis: Long,
    val artworkUrl600: String,
    val episodeUrl: String,
    val episodeFileExtension: String,
): Parcelable
