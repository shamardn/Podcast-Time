package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class GetSubscriptionsUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke(): List<PodcastEntity> {
        return podcastRepo.getSubscriptions()
    }
}