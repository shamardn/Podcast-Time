package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.datasource.local.database.entity.RecentEntity
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import javax.inject.Inject

class DeletePodcastFromRecentUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke(podcast: RecentEntity) {
        podcastRepo.deletePodcastFromRecent(podcast)
    }
}