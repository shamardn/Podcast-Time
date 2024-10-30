package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.mapper.PodcastDTOMapper
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo

class GetLocalPodcastsUseCase(
    private val repo: PodcastRepo,
    private val mapper: PodcastDTOMapper,
) {
    suspend operator fun invoke(): List<PodcastEntity> {
        if (isLocalDataAvailable()) {
            return repo.getLocalPodcasts()
        }
        val podcastsDTO = repo.refreshRemotePodcasts().results
        repo.insertAllPodcasts(mapper.mapList(podcastsDTO))
        return mapper.mapList(podcastsDTO)
    }

    private suspend fun isLocalDataAvailable(): Boolean {
        return repo.getLocalPodcasts().isNotEmpty()
    }
}
