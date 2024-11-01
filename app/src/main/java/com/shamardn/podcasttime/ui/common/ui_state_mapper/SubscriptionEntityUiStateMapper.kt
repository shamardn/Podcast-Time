package com.shamardn.podcasttime.ui.common.ui_state_mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.SubscriptionsEntity
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class SubscriptionEntityUiStateMapper @Inject constructor(
) : Mapper<PodcastUiState, SubscriptionsEntity>() {
    override fun map(input: PodcastUiState): SubscriptionsEntity {
        return SubscriptionsEntity(
            trackId = input.trackId,
            artistName = input.artistName,
            artworkUrl600 = input.artworkUrl600,
            trackCount = input.trackCount,
            trackName = input.trackName,
            collectionName = input.trackName,
        )
    }

    override fun reverseMap(input: SubscriptionsEntity): PodcastUiState {
        return PodcastUiState(
            trackId = input.trackId,
            artistName = input.artistName,
            artworkUrl600 = input.artworkUrl600,
            trackCount = input.trackCount,
            trackName = input.trackName,
            collectionName = input.trackName,
            primaryGenreName = "" ,
            releaseDate = "",
        )
    }
}