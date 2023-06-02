package com.shamardn.podcasttime.di

import com.shamardn.podcasttime.domain.repo.PodcastRepo
import com.shamardn.podcasttime.domain.usecase.GetPodcastByIdUseCase
import com.shamardn.podcasttime.domain.usecase.GetPodcastsUseCase
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
}