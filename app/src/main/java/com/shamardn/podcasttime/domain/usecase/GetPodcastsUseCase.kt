package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.mapper.PodcastDTOMapper
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
class GetPodcastsUseCase (
    private val repo: PodcastRepo,
    private val podcastDTOMapper: PodcastDTOMapper,
) {
    suspend operator fun invoke() : List<PodcastEntity> {
        return podcastDTOMapper.mapList(repo.refreshRemotePodcasts().results)
    }
}