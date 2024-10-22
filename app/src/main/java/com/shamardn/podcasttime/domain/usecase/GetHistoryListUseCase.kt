package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.datasource.local.database.entity.HistoryEntity
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import javax.inject.Inject

class GetHistoryListUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke(): List<HistoryEntity> {
        return podcastRepo.getHistoryList()
    }
}