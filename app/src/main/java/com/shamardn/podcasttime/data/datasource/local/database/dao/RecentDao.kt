package com.shamardn.podcasttime.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shamardn.podcasttime.data.datasource.local.database.entity.RecentEntity

@Dao
interface RecentDao {
    @Query("SELECT * FROM RECENT_TABLE ORDER BY savedTime DESC")
    suspend fun getRecentList(): List<RecentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToRecent(podcast: RecentEntity)

    @Delete
    suspend fun deletePodcastFromRecent(podcast: RecentEntity)

    @Query("DELETE FROM RECENT_TABLE")
    suspend fun deleteRecentList()
}