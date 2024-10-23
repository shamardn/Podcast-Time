package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.datasource.local.database.entity.HistoryEntity
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import javax.inject.Inject

class SaveToHistoryUseCase @Inject constructor (
    private val repo: PodcastRepo
    ) {
    suspend operator fun invoke(historyEntity: HistoryEntity)  = repo.saveToHistory(historyEntity)
}