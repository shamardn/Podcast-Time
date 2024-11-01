package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.mapper.EpisodeEntityDTOMapper
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo

class GetEpisodesByIdUseCase(
    private val repo: PodcastRepo,
    private val episodeEntityDTOMapper: EpisodeEntityDTOMapper,
) {
    suspend operator fun invoke(trackId: Long): List<EpisodeEntity> {
        return episodeEntityDTOMapper.mapList(repo.refreshEpisodesById(trackId).results)
    }
}