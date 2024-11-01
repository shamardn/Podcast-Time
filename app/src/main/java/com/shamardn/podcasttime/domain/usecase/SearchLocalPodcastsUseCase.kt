package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.ui_state_mapper.PodcastEntityUiStateMapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState

class SearchLocalPodcastsUseCase (
    private val repo: PodcastRepo,
    private val mapper: PodcastEntityUiStateMapper,
) {
    suspend operator fun invoke(text: String) : List<PodcastUiState> {
        return mapper.mapList(repo.searchLocalPodcastsByName(text))
    }
}