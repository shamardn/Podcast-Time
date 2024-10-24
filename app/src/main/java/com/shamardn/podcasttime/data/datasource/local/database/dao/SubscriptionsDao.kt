package com.shamardn.podcasttime.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shamardn.podcasttime.data.datasource.local.database.entity.SubscriptionsEntity

@Dao
interface SubscriptionsDao {
    @Query("SELECT * FROM SUBSCRIPTIONS_TABLE ORDER BY collectionName")
    suspend fun getAllSubscriptionsOrderedByName(): List<SubscriptionsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun subscribe(entity: SubscriptionsEntity)

    @Delete
    suspend fun unsubscribe(entity: SubscriptionsEntity)

    @Query("DELETE FROM SUBSCRIPTIONS_TABLE")
    suspend fun deleteSubscriptionList()
}