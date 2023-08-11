package com.shamardn.podcasttime.di

import android.content.Context
import androidx.room.Room
import com.shamardn.podcasttime.data.local.database.PodcastDatabase
import com.shamardn.podcasttime.data.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.local.database.dao.PodcastDao
import com.shamardn.podcasttime.data.local.database.dao.SubscriptionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providesRoomDatabase(
        @ApplicationContext context: Context,
    ): PodcastDatabase =
        Room.databaseBuilder(context, PodcastDatabase::class.java, "PodcastDatabase")
            .build()

    @Singleton
    @Provides
    fun provideEpisodeDao(podcastDatabase: PodcastDatabase): EpisodeDao {
        return podcastDatabase.episodeDao()
    }

    @Singleton
    @Provides
    fun providePodcastDao(podcastDatabase: PodcastDatabase): PodcastDao {
        return podcastDatabase.podcastDao()
    }
    @Singleton
    @Provides
    fun provideSubscriptionsDao(podcastDatabase: PodcastDatabase): SubscriptionsDao {
        return podcastDatabase.subscriptionDao()
    }
}