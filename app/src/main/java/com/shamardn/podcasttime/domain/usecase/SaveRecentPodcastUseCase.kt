package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.ui_state_mapper.RecentEntityUiStateMapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState

class SaveRecentPodcastUseCase(
    private val repo: PodcastRepo,
    private val mapper: RecentEntityUiStateMapper,
    ) {
    suspend operator fun invoke(podcast: PodcastUiState) {
        repo.saveToRecent(mapper.reverseMap(podcast))
    }
}