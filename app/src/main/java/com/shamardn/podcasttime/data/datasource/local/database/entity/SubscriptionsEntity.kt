package com.shamardn.podcasttime.data.datasource.local.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "SUBSCRIPTIONS_TABLE")
data class SubscriptionsEntity(
    @PrimaryKey(autoGenerate = true)
    val trackId: Long,
    val artistName: String,
    val artworkUrl600: String,
    val trackName: String,
    val trackCount: Int,
    val collectionName: String,
): Parcelable
