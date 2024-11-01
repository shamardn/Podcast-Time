package com.shamardn.podcasttime.data.datasource.local.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "RECENT_TABLE")
data class RecentEntity(
    @PrimaryKey(autoGenerate = true)
    val trackId: Long,
    val artistName: String,
    val collectionName: String,
    val artworkUrl600: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val trackCount: Int,
    val trackName: String,
    val savedTime: Long,
): Parcelable
