package com.shamardn.podcasttime.ui.home.mapper

import com.shamardn.podcasttime.domain.entity.PodcastDTO
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.home.uistate.PodcastUiState
import javax.inject.Inject

class PodcastUiStateMapper @Inject constructor(

): Mapper<PodcastDTO, PodcastUiState>() {
    override fun map(input: PodcastDTO): PodcastUiState {
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