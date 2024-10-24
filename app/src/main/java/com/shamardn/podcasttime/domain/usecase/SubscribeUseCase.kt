package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.mapper.SubscriptionUIMapper
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class SubscribeUseCase @Inject constructor(
    private val repo: PodcastRepo,
    private val subscriptionUIMapper: SubscriptionUIMapper,
) {
    suspend operator fun invoke(podcastUiState: PodcastUiState) {
        val subscriptionsEntity = subscriptionUIMapper.map(podcastUiState)
        repo.subscribe(subscriptionsEntity)
    }
}