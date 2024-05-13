package com.shamardn.podcasttime.ui.history.mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.HistoryEntity
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.history.uistate.PodcastUiState
import javax.inject.Inject

class HistoryUiStateMapper @Inject constructor(

): Mapper<HistoryEntity, PodcastUiState>() {
    override fun map(input: HistoryEntity): PodcastUiState {
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