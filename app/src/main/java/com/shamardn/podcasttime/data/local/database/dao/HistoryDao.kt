package com.shamardn.podcasttime.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shamardn.podcasttime.data.local.database.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HISTORY_TABLE ORDER BY savedTime DESC")
    suspend fun getHistoryList(): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToHistory(podcast: HistoryEntity)

    @Delete
    suspend fun deletePodcastFromHistory(podcast: HistoryEntity)

    @Query("DELETE FROM HISTORY_TABLE")
    suspend fun deleteHistoryList()
}