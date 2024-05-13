package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class GetDownloadedEpisodesUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke(): List<EpisodeEntity> {
       return podcastRepo.getDownloadedEpisodes()
    }
}