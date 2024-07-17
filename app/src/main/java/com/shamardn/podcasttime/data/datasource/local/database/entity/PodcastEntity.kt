package com.shamardn.podcasttime.data.datasource.local.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "SUBSCRIPTIONS_TABLE")
data class PodcastEntity(
    @PrimaryKey(autoGenerate = true)
    val trackId: Long,
    val artistName: String,
    val collectionName: String,
    val artworkUrl100: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val trackCount: Int,
    val trackName: String,
): Parcelable
