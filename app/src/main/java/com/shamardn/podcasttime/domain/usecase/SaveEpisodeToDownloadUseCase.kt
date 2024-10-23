package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.mapper.EpisodeUiStateToDownloadEntityMapper
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import javax.inject.Inject

class SaveEpisodeToDownloadUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
    private val mapper: EpisodeUiStateToDownloadEntityMapper,
) {

    suspend operator fun invoke(episodeUiState: EpisodeUiState) {
        podcastRepo.saveEpisodeToDownload(mapper.map(episodeUiState))
    }
}