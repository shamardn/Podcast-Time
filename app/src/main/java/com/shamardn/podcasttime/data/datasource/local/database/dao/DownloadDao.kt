package com.shamardn.podcasttime.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeDownloadEntity

@Dao
interface DownloadDao {

    @Query("SELECT * FROM EPISODE_DOWNLOAD_TABLE ORDER BY id DESC")
    suspend fun getAllDownloadedEpisodes(): List<EpisodeDownloadEntity>

    @Query("SELECT * FROM EPISODE_TABLE WHERE trackName LIKE '%' || :text || '%'")
    suspend fun getDownloadedEpisodeByName(text: String): List<EpisodeDownloadEntity>

    @Query("SELECT * FROM EPISODE_TABLE WHERE id = :id")
    suspend fun getDownloadedEpisodeById(id :String): EpisodeDownloadEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun downloadEpisode(episode: EpisodeDownloadEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEpisodes(episodes: List<EpisodeDownloadEntity>)

    @Delete
    suspend fun deleteDownloadedEpisode(episode: EpisodeDownloadEntity)

    @Query("DELETE FROM EPISODE_DOWNLOAD_TABLE")
    suspend fun deleteAllDownloadedEpisodes()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: EpisodeDownloadEntity)
}
