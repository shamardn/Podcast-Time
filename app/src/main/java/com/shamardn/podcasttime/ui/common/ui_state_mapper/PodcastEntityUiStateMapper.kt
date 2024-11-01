package com.shamardn.podcasttime.ui.common.ui_state_mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class PodcastEntityUiStateMapper @Inject constructor(

) : Mapper<PodcastEntity, PodcastUiState>() {
    override fun map(input: PodcastEntity): PodcastUiState {
        return PodcastUiState(
            trackId = input.trackId,
            trackName = input.trackName,
            artworkUrl600 = input.artworkUrl600,
            releaseDate = input.releaseDate,
            trackCount = input.trackCount,
            primaryGenreName = input.primaryGenreName,
            artistName = input.artistName,
            collectionName = input.collectionName,
        )
    }

    override fun reverseMap(input: PodcastUiState): PodcastEntity {
        return PodcastEntity(
            trackId = input.trackId,
            trackName = input.trackName,
            artworkUrl600 = input.artworkUrl600,
            trackCount = input.trackCount,
            artistName = input.artistName,
            primaryGenreName = input.primaryGenreName,
            releaseDate = input.releaseDate,
            collectionName = input.collectionName,
        )
    }
}