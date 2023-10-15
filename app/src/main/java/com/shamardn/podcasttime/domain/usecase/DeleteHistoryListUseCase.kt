package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class DeleteHistoryListUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke() {
        podcastRepo.deleteHistoryList()
    }
}