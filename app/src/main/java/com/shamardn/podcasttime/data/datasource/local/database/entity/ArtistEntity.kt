package com.shamardn.podcasttime.data.datasource.local.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ARTIST_TABLE")
data class ArtistEntity (
    @PrimaryKey(autoGenerate = true)
    val artistId: Int,
    val artistName: String,
    val artworkUrl100: String,
    val artworkUrl160: String,
    val collectionId: Int,
    val collectionName: String,
): Parcelable