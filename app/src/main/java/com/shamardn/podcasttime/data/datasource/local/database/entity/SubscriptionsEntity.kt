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
    val artworkUrl100: String,
    val trackName: String,
    val trackCount: Int,
    val collectionName: String,
//    val artistViewUrl: String,
//    val artworkUrl30: String,
//    val artworkUrl60: String,
//    val artworkUrl600: String,
//    val collectionCensoredName: String,
//    val collectionExplicitness: String,
//    val collectionHdPrice: Int,
//    val collectionPrice: Double,
//    val collectionViewUrl: String,
//    val contentAdvisoryRating: String,
//    val country: String,
//    val currency: String,
//    val feedUrl: String,
//    val genreIds: List<String>,
//    val genres: List<String>,
//    val kind: String,
//    val primaryGenreName: String,
//    val releaseDate: String,
//    val trackCensoredName: String,
//    val trackExplicitness: String,
//    val trackPrice: Double,
//    val trackTimeMillis: Int,
//    val trackViewUrl: String,
//    val wrapperType: String
): Parcelable
