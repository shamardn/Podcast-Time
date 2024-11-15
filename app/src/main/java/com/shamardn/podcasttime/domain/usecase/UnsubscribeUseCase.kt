package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.mapper.UiStateSubscriptionMapper
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class UnsubscribeUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
    private val mapper: UiStateSubscriptionMapper,
) {
    suspend operator fun invoke(entity: PodcastUiState) {
        podcastRepo.unsubscribe(mapper.map(entity))
    }
}