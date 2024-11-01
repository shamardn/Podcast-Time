package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.ui_state_mapper.RecentEntityUiStateMapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class GetRecentPodcastsUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
    private val mapper: RecentEntityUiStateMapper,
) {
    suspend operator fun invoke(): List<PodcastUiState> {
        return mapper.mapList(podcastRepo.getRecentList())
    }
}