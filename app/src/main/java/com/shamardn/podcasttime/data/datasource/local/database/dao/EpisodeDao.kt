package com.shamardn.podcasttime.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity

@Dao
interface EpisodeDao {
    @Query("SELECT * FROM EPISODE_TABLE ORDER BY collectionName")
    suspend fun getAllEpisodes(): List<EpisodeEntity>

    @Query("SELECT * FROM EPISODE_TABLE WHERE trackName LIKE '%' || :text || '%'")
    suspend fun getEpisodeByName(text: String): List<EpisodeEntity>

    @Query("SELECT * FROM EPISODE_TABLE WHERE id = :id")
    suspend fun getEpisodeById(id :String): EpisodeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episodeEntity: EpisodeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEpisodes(episodes: List<EpisodeEntity>)

    @Delete
    suspend fun deleteEpisode(episodeEntity: EpisodeEntity)

    @Query("DELETE FROM EPISODE_TABLE")
    suspend fun deleteAllEpisodes()
}