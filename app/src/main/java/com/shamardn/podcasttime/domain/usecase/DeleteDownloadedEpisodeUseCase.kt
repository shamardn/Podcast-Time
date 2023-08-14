package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class DeleteDownloadedEpisodeUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke(episodeEntity: EpisodeEntity) {
        podcastRepo.deleteEpisode(episodeEntity)
    }
}