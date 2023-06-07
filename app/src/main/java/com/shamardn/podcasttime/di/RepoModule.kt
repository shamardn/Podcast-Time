package com.shamardn.podcasttime.di

import com.shamardn.podcasttime.data.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.remote.ApiService
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
    fun provideRepo(apiService: ApiService, episodeDao: EpisodeDao): PodcastRepo{
        return PodcastRepoImpl(apiService, episodeDao)
    }
}