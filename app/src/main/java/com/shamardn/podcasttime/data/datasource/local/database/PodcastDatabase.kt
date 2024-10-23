package com.shamardn.podcasttime.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shamardn.podcasttime.data.datasource.local.database.dao.ArtistDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.DownloadDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.HistoryDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.PlaylistDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.PodcastDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.SubscriptionsDao
import com.shamardn.podcasttime.data.datasource.local.database.entity.ArtistEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeDownloadEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.HistoryEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.PlaylistEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.SubscriptionsEntity

@Database(
    entities = [PodcastEntity::class, EpisodeEntity::class, SubscriptionsEntity::class, HistoryEntity::class, ArtistEntity::class, EpisodeDownloadEntity::class, PlaylistEntity::class],
    version = 1
)
@TypeConverters(value = [RoomTypeConverters::class])
abstract class PodcastDatabase: RoomDatabase() {
    abstract fun podcastDao(): PodcastDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun subscriptionDao(): SubscriptionsDao
    abstract fun historyDao(): HistoryDao
    abstract fun artistDao(): ArtistDao
    abstract fun downloadDao(): DownloadDao
    abstract fun playlistDao(): PlaylistDao
}