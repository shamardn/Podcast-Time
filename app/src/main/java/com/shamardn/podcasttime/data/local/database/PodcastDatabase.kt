package com.shamardn.podcasttime.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shamardn.podcasttime.data.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity

@Database(entities = [EpisodeEntity::class], version = 1)
abstract class PodcastDatabase: RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
}