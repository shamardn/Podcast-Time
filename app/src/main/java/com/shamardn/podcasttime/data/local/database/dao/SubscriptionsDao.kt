package com.shamardn.podcasttime.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity

@Dao
interface SubscriptionsDao {
    @Query("SELECT * FROM SUBSCRIPTIONS_TABLE ORDER BY collectionName")
    suspend fun getSubscriptions(): List<PodcastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun subscribe(podcast: PodcastEntity)

    @Delete
    suspend fun unsubscribe(podcast: PodcastEntity)
}