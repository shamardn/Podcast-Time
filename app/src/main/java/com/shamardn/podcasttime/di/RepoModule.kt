package com.shamardn.podcasttime.di

import com.shamardn.podcasttime.data.datasource.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.HistoryDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.PodcastDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.SubscriptionsDao
import com.shamardn.podcasttime.data.datasource.remote.ApiService
import com.shamardn.podcasttime.data.repo.PodcastRepoImpl
import com.shamardn.podcasttime.domain.repo.PodcastRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideRepo(
        apiService: ApiService,
        episodeDao: EpisodeDao,
        podcastDao: PodcastDao,
        subscriptionsDao: SubscriptionsDao,
        historyDao: HistoryDao,
    ): PodcastRepo {
        return PodcastRepoImpl(apiService, episodeDao, podcastDao, subscriptionsDao, historyDao)
    }
}