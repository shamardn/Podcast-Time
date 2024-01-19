package com.shamardn.podcasttime.ui.subscriptions.mapper

import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.subscriptions.uistate.PodcastUiState
import javax.inject.Inject

class SubscriptionsUiStateMapper @Inject constructor(

): Mapper<PodcastEntity, PodcastUiState>() {
    override fun map(input: PodcastEntity): PodcastUiState {
        return PodcastUiState(
            trackId = input.trackId,
            trackName = input.trackName,
            artworkUrl100 = input.artworkUrl100,
            releaseDate = input.releaseDate,
            trackCount = input.trackCount,
            primaryGenreName = input.primaryGenreName,
            artistName = input.artistName,
        )
    }
}