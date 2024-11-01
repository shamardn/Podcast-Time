package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.ui_state_mapper.SubscriptionEntityUiStateMapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class SubscribeUseCase @Inject constructor(
    private val repo: PodcastRepo,
    private val subscriptionEntityUIMapper: SubscriptionEntityUiStateMapper,
) {
    suspend operator fun invoke(podcastUiState: PodcastUiState) {
        val subscriptionsEntity = subscriptionEntityUIMapper.map(podcastUiState)
        repo.subscribe(subscriptionsEntity)
    }
}