package com.shamardn.podcasttime.ui.common.ui_state_mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.RecentEntity
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class HistoryEntityUiStateMapper @Inject constructor(

): Mapper<PodcastUiState, RecentEntity>() {
    override fun map(input: PodcastUiState): RecentEntity {
        return RecentEntity(
            trackId = input.trackId,
            trackName = input.trackName,
            artworkUrl600 = input.artworkUrl600,
            releaseDate = input.releaseDate,
            trackCount = input.trackCount,
            primaryGenreName = input.primaryGenreName,
            artistName = input.artistName,
            collectionName = input.collectionName,
            savedTime = 0,
        )
    }

    override fun reverseMap(input: RecentEntity): PodcastUiState {
        return PodcastUiState(
            trackId = input.trackId,
            artistName = input.artistName,
            collectionName = input.collectionName,
            artworkUrl600 = input.artworkUrl600,
            primaryGenreName = input.primaryGenreName,
            releaseDate = input.releaseDate,
            trackCount = input.trackCount,
            trackName = input.trackName,
        )
    }
}