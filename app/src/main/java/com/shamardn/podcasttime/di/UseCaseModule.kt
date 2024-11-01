package com.shamardn.podcasttime.di

import com.shamardn.podcasttime.domain.mapper.EpisodeEntityDTOMapper
import com.shamardn.podcasttime.domain.mapper.PodcastEntityDTOMapper
import com.shamardn.podcasttime.ui.common.ui_state_mapper.SubscriptionEntityUiStateMapper
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.domain.usecase.GetEpisodesByIdUseCase
import com.shamardn.podcasttime.domain.usecase.GetFavouritePlaylistUseCase
import com.shamardn.podcasttime.domain.usecase.GetRecentListUseCase
import com.shamardn.podcasttime.domain.usecase.GetLocalPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.GetPodcastByIdUseCase
import com.shamardn.podcasttime.domain.usecase.GetPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.GetSubscriptionsUseCase
import com.shamardn.podcasttime.domain.usecase.SaveAllPodcastsLocallyUseCase
import com.shamardn.podcasttime.domain.usecase.SaveRecentPodcastUseCase
import com.shamardn.podcasttime.domain.usecase.SearchLocalPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.UnsubscribeUseCase
import com.shamardn.podcasttime.ui.common.ui_state_mapper.PodcastEntityUiStateMapper
import com.shamardn.podcasttime.ui.common.ui_state_mapper.PodcastUiStateDTOMapper
import com.shamardn.podcasttime.ui.common.ui_state_mapper.RecentEntityUiStateMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providePodcastsUseCase(repo: PodcastRepo, podcastUiStateDTOMapper: PodcastUiStateDTOMapper): GetPodcastsUseCase{
        return GetPodcastsUseCase(repo, podcastUiStateDTOMapper)
    }

    @Provides
    fun provideGetEpisodesByIdUseCase(repo: PodcastRepo, mapper: EpisodeEntityDTOMapper): GetEpisodesByIdUseCase {
        return GetEpisodesByIdUseCase (repo, mapper)
    }

    @Provides
    fun provideSaveRecentPodcastUseCase (repo: PodcastRepo, mapper: RecentEntityUiStateMapper): SaveRecentPodcastUseCase {
        return SaveRecentPodcastUseCase(repo, mapper)
    }

    @Provides
    fun provideSaveAllPodcastsLocallyUseCase (repo: PodcastRepo, mapper: PodcastEntityUiStateMapper ): SaveAllPodcastsLocallyUseCase {
        return SaveAllPodcastsLocallyUseCase(repo, mapper)
    }

    @Provides
    fun provideGetLocalPodcastsUseCase (repo: PodcastRepo,
                                        podcastEntityUiStateMapper: PodcastEntityUiStateMapper,
                                        podcastEntityDTOMapper: PodcastEntityDTOMapper,
                                        podcastUiStateDTOMapper: PodcastUiStateDTOMapper
    ): GetLocalPodcastsUseCase {
        return GetLocalPodcastsUseCase(repo, podcastEntityUiStateMapper, podcastEntityDTOMapper, podcastUiStateDTOMapper )
    }

    @Provides
    fun provideGetPodcastByIdUseCase(repo: PodcastRepo): GetPodcastByIdUseCase{
        return GetPodcastByIdUseCase(repo,)
    }

    @Provides
    fun provideSubscribedPodcastsUseCase(repo: PodcastRepo, mapper: SubscriptionEntityUiStateMapper): GetSubscriptionsUseCase{
        return GetSubscriptionsUseCase(repo, mapper)
    }

    @Provides
    fun provideUnSubscribedPodcastsUseCase(repo: PodcastRepo, mapper: SubscriptionEntityUiStateMapper): UnsubscribeUseCase{
        return UnsubscribeUseCase(repo, mapper)
    }

    @Provides
    fun provideHistoryListUseCase(repo: PodcastRepo): GetRecentListUseCase {
        return GetRecentListUseCase(repo)
    }

    @Provides
    fun provideSearchLocalPodcastsUseCase(repo: PodcastRepo, mapper: PodcastEntityUiStateMapper): SearchLocalPodcastsUseCase {
        return SearchLocalPodcastsUseCase(repo, mapper)
    }

    @Provides
    fun provideGetFavouritePlaylistUseCase(repo: PodcastRepo): GetFavouritePlaylistUseCase {
        return GetFavouritePlaylistUseCase(repo)
    }
}