package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.ui_state_mapper.PodcastEntityUiStateMapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class SaveAllPodcastsLocallyUseCase @Inject constructor (
    private val repo: PodcastRepo,
    private val mapper: PodcastEntityUiStateMapper,
    ) {
    suspend operator fun invoke(podcasts: List<PodcastUiState>)  = repo.insertAllPodcasts(podcasts.map{mapper.reverseMap(it)})
}