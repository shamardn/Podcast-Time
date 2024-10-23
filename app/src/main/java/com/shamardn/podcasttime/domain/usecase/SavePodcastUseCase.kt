package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.mapper.PodcastUiStateMapper.Companion.toEntity
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState

class SavePodcastUseCase(
    private val repo: PodcastRepo,
    ) {
    suspend operator fun invoke(podcast: PodcastUiState) {
        repo.insertPodcast(podcast.toEntity())
    }
}