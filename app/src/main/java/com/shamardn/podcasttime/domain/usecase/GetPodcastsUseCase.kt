package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.ui_state_mapper.PodcastUiStateDTOMapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState

class GetPodcastsUseCase (
    private val repo: PodcastRepo,
    private val mapper: PodcastUiStateDTOMapper,
) {
    suspend operator fun invoke() : List<PodcastUiState> {
        return mapper.mapList(repo.refreshRemotePodcasts().results)
    }
}