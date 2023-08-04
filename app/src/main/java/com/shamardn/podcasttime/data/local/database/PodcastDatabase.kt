package com.shamardn.podcasttime.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shamardn.podcasttime.data.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.local.database.dao.PodcastDao
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.data.local.database.entity.EpisodeAudio

@Database(
    entities = [EpisodeEntity::class, EpisodeAudio::class],
    version = 1
)
abstract class PodcastDatabase: RoomDatabase() {
    abstract fun podcastDao(): PodcastDao
    abstract fun episodeDao(): EpisodeDao
}