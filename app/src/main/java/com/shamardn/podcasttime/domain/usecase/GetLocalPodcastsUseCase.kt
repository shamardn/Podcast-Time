package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.mapper.PodcastEntityDTOMapper
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.ui_state_mapper.PodcastEntityUiStateMapper
import com.shamardn.podcasttime.ui.common.ui_state_mapper.PodcastUiStateDTOMapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState

class GetLocalPodcastsUseCase(
    private val repo: PodcastRepo,
    private val podcastEntityUiStateMapper: PodcastEntityUiStateMapper,
    private val podcastEntityDTOMapper: PodcastEntityDTOMapper,
    private val podcastUiStateDTOMapper: PodcastUiStateDTOMapper
) {
    suspend operator fun invoke(): List<PodcastUiState> {
        if (isLocalDataAvailable()) {
            return podcastEntityUiStateMapper.mapList(repo.getLocalPodcasts())
        }
        val podcastsDTO = repo.refreshRemotePodcasts().results
        repo.insertAllPodcasts(podcastEntityDTOMapper.mapList(podcastsDTO))
        return podcastUiStateDTOMapper.mapList(podcastsDTO)
    }

    private suspend fun isLocalDataAvailable(): Boolean {
        return repo.getLocalPodcasts().isNotEmpty()
    }
}
