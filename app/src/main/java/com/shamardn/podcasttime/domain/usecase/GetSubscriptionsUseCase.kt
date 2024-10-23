package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.mapper.UiStateSubscriptionMapper
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class GetSubscriptionsUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
) {
    suspend operator fun invoke(): List<PodcastUiState> {
        return podcastRepo.getSubscriptions().map {
            UiStateSubscriptionMapper.toUiState(it)
        }
    }
}