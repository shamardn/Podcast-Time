package com.shamardn.podcasttime.data.datasource.local.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "PLAYLIST_TABLE")
data class PlaylistEntity(
    @PrimaryKey
    val playlistName:String,
    val playlistEpisodes :MutableList<EpisodeEntity>,
): Parcelable
