package com.shamardn.podcasttime.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shamardn.podcasttime.data.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.local.database.dao.PodcastDao
import com.shamardn.podcasttime.data.local.database.dao.SubscriptionsDao
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.data.local.database.entity.EpisodeAudio
import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity

@Database(
    entities = [EpisodeEntity::class, EpisodeAudio::class, PodcastEntity::class],
    version = 1
)
abstract class PodcastDatabase: RoomDatabase() {
    abstract fun podcastDao(): PodcastDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun subscriptionDao(): SubscriptionsDao
}