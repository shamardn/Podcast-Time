package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import javax.inject.Inject

class DeleteRecentListUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke() {
        podcastRepo.deleteRecentList()
    }
}