package com.shamardn.podcasttime.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity

@Dao
interface PodcastDao {
    @Query("SELECT * FROM PODCAST_TABLE")
    suspend fun getLocalPodcasts(): List<PodcastEntity>

    @Query("SELECT * FROM PODCAST_TABLE WHERE trackId = :podcastId")
    suspend fun getPodcastById(podcastId: Long): PodcastEntity

    @Query("SELECT * FROM PODCAST_TABLE WHERE collectionName LIKE '%' || :text || '%'")
    suspend fun searchLocalPodcastsByName(text: String): List<PodcastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPodcasts(podcasts: List<PodcastEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPodcast(podcast: PodcastEntity)

    @Delete
    suspend fun deletePodcast(podcast: PodcastEntity)

    @Query("DELETE FROM PODCAST_TABLE")
    suspend fun deleteAllPodcasts()
}