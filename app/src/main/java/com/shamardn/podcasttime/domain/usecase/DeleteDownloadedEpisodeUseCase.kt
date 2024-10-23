package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import com.shamardn.podcasttime.ui.download.DownloadEpisodeMapper
import javax.inject.Inject

class DeleteDownloadedEpisodeUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke(episode: EpisodeUiState) {
        podcastRepo.deleteDownloadedEpisode(DownloadEpisodeMapper.toEpisodeDownloadEntity(episode))
    }
}