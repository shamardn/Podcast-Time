package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.ui.common.ui_state_mapper.SubscriptionEntityUiStateMapper
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class GetSubscriptionsUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
    private val mapper: SubscriptionEntityUiStateMapper,
) {
    suspend operator fun invoke(): List<PodcastUiState> {
        return podcastRepo.getSubscriptions().map {
            mapper.reverseMap(it)
        }
    }
}