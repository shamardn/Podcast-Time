package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class GetEpisodeByGuidUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke(guid: String): EpisodeEntity {
       return podcastRepo.getEpisodeByGuid(guid)
    }
}