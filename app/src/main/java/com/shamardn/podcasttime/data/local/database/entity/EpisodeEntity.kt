package com.shamardn.podcasttime.data.local.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "EPISODE_TABLE")
data class EpisodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val collectionName: String,
    val trackName: String,
    val releaseDate: String,
    val description: String,
    val trackTimeMillis: Int,
    val artworkUrl160: String,
    val episodeUrl: String,
    val guid: String,
    val episodeFileExtension: String,
): Parcelable
