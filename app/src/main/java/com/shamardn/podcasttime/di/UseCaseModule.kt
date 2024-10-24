package com.shamardn.podcasttime.di

import com.shamardn.podcasttime.domain.mapper.EpisodeDTOMapper
import com.shamardn.podcasttime.domain.mapper.PodcastDTOMapper
import com.shamardn.podcasttime.domain.mapper.UiStateSubscriptionMapper
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.domain.usecase.GetEpisodesByIdUseCase
import com.shamardn.podcasttime.domain.usecase.GetFavouritePlaylistUseCase
import com.shamardn.podcasttime.domain.usecase.GetHistoryListUseCase
import com.shamardn.podcasttime.domain.usecase.GetPodcastByIdUseCase
import com.shamardn.podcasttime.domain.usecase.GetPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.GetSubscriptionsUseCase
import com.shamardn.podcasttime.domain.usecase.SavePodcastUseCase
import com.shamardn.podcasttime.domain.usecase.SearchLocalPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.UnsubscribeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providePodcastsUseCase(repo: PodcastRepo, podcastDTOMapper: PodcastDTOMapper): GetPodcastsUseCase{
        return GetPodcastsUseCase(repo, podcastDTOMapper)
    }

    @Provides
    fun provideGetEpisodesByIdUseCase(repo: PodcastRepo, mapper: EpisodeDTOMapper): GetEpisodesByIdUseCase {
        return GetEpisodesByIdUseCase (repo, mapper)
    }

    @Provides
    fun provideSavePodcastUseCase (repo: PodcastRepo): SavePodcastUseCase {
        return SavePodcastUseCase(repo)
    }

    @Provides
    fun provideGetPodcastByIdUseCase(repo: PodcastRepo): GetPodcastByIdUseCase{
        return GetPodcastByIdUseCase(repo,)
    }

    @Provides
    fun provideSubscribedPodcastsUseCase(repo: PodcastRepo): GetSubscriptionsUseCase{
        return GetSubscriptionsUseCase(repo)
    }

    @Provides
    fun provideUnSubscribedPodcastsUseCase(repo: PodcastRepo, mapper: UiStateSubscriptionMapper): UnsubscribeUseCase{
        return UnsubscribeUseCase(repo, mapper)
    }

    @Provides
    fun provideHistoryListUseCase(repo: PodcastRepo): GetHistoryListUseCase {
        return GetHistoryListUseCase(repo)
    }

    @Provides
    fun provideSearchLocalPodcastsUseCase(repo: PodcastRepo): SearchLocalPodcastsUseCase {
        return SearchLocalPodcastsUseCase(repo)
    }

    @Provides
    fun provideGetFavouritePlaylistUseCase(repo: PodcastRepo): GetFavouritePlaylistUseCase {
        return GetFavouritePlaylistUseCase(repo)
    }
}