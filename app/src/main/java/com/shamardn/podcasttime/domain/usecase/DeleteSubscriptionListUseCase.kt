package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import javax.inject.Inject

class DeleteSubscriptionListUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke() {
        podcastRepo.deleteSubscriptionList()
    }
}