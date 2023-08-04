package com.shamardn.podcasttime.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shamardn.podcasttime.data.local.database.entity.EpisodeAudio

@Dao
interface PodcastDao {
    @Query("SELECT * FROM PODCAST_TABLE")
    suspend fun getMediaPodcasts(): List<EpisodeAudio>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<EpisodeAudio>)
}