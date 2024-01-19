package com.shamardn.podcasttime.di

import com.shamardn.podcasttime.domain.repo.PodcastRepo
import com.shamardn.podcasttime.domain.usecase.GetHistoryListUseCase
import com.shamardn.podcasttime.domain.usecase.GetMediaPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.GetPodcastByIdUseCase
import com.shamardn.podcasttime.domain.usecase.GetPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.GetSubscriptionsUseCase
import com.shamardn.podcasttime.domain.usecase.UnsubscribeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providePodcastsUseCase(repo: PodcastRepo): GetPodcastsUseCase{
        return GetPodcastsUseCase(repo)
    }

    @Provides
    fun providePodcastByIdUseCase(repo: PodcastRepo): GetPodcastByIdUseCase{
        return GetPodcastByIdUseCase(repo)
    }

    @Provides
    fun provideMediaPodcastsUseCase(repo: PodcastRepo): GetMediaPodcastsUseCase{
        return GetMediaPodcastsUseCase(repo)
    }

    @Provides
    fun provideSubscribedPodcastsUseCase(repo: PodcastRepo): GetSubscriptionsUseCase{
        return GetSubscriptionsUseCase(repo)
    }

    @Provides
    fun provideUnSubscribedPodcastsUseCase(repo: PodcastRepo): UnsubscribeUseCase{
        return UnsubscribeUseCase(repo)
    }

    @Provides
    fun provideHistoryListUseCase(repo: PodcastRepo): GetHistoryListUseCase {
        return GetHistoryListUseCase(repo)
    }
}