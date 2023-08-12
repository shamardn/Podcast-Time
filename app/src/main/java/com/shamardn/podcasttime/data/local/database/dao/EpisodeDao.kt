package com.shamardn.podcasttime.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity

@Dao
interface EpisodeDao {
    @Query("SELECT * FROM EPISODE_TABLE ORDER BY collectionName")
    suspend fun getEpisodes(): List<EpisodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episodeEntity: EpisodeEntity)

    @Delete
    suspend fun deleteEpisode(episodeEntity: EpisodeEntity)

    @Query("DELETE FROM EPISODE_TABLE")
    suspend fun deleteAllEpisodes()
}