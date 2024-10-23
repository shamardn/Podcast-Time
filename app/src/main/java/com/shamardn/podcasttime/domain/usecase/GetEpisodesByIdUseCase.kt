package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.mapper.EpisodeDTOMapper
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo

class GetEpisodesByIdUseCase(
    private val repo: PodcastRepo,
    private val episodeDTOMapper: EpisodeDTOMapper,
) {
    suspend operator fun invoke(trackId: Long): List<EpisodeEntity> {
        return episodeDTOMapper.mapList(repo.refreshEpisodesById(trackId).results)
    }
}