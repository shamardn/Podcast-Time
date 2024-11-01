package com.shamardn.podcasttime.di

import android.content.Context
import androidx.room.Room
import com.shamardn.podcasttime.data.datasource.local.database.PodcastDatabase
import com.shamardn.podcasttime.data.datasource.local.database.dao.ArtistDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.DownloadDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.RecentDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.PlaylistDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.PodcastDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.SubscriptionsDao
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
    fun provideRecentDao(podcastDatabase: PodcastDatabase): RecentDao {
        return podcastDatabase.recentDao()
    }

    @Singleton
    @Provides
    fun provideSubscriptionsDao(podcastDatabase: PodcastDatabase): SubscriptionsDao {
        return podcastDatabase.subscriptionDao()
    }

    @Singleton
    @Provides
    fun provideDownloadDao(podcastDatabase: PodcastDatabase): DownloadDao {
        return podcastDatabase.downloadDao()
    }

    @Singleton
    @Provides
    fun provideArtistDao(podcastDatabase: PodcastDatabase): ArtistDao {
        return podcastDatabase.artistDao()
    }

    @Singleton
    @Provides
    fun providePlaylistDao(podcastDatabase: PodcastDatabase): PlaylistDao {
        return podcastDatabase.playlistDao()
    }

}