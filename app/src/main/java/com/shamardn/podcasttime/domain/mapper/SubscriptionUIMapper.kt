package com.shamardn.podcasttime.domain.mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.SubscriptionsEntity
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class SubscriptionUIMapper @Inject constructor(
) : Mapper<PodcastUiState, SubscriptionsEntity>() {
    override fun map(input: PodcastUiState): SubscriptionsEntity {
        return SubscriptionsEntity(
            trackId = input.trackId,
            artistName = input.artistName,
            artworkUrl100 = input.artworkUrl100,
            trackCount = input.trackCount,
            trackName = input.trackName,
            collectionName = input.trackName,
        )
    }
}