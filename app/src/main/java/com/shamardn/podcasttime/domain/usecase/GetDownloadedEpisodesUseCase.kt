package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.mapper.EpisodeUiStateToDownloadEntityMapper
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import javax.inject.Inject

class GetDownloadedEpisodesUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke(): List<EpisodeUiState> {
       return podcastRepo.getAllDownloadedEpisodes().map { EpisodeUiStateToDownloadEntityMapper.toEpisodeUiState(it) }
    }
}