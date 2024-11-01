package com.shamardn.podcasttime.ui.common.ui_state_mapper

import com.shamardn.podcasttime.data.datasource.remote.dtos.PodcastDTO
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class PodcastUiStateDTOMapper @Inject constructor(
) : Mapper<PodcastDTO, PodcastUiState>(){
    override fun map(input: PodcastDTO): PodcastUiState {
        return PodcastUiState(
            trackId = input.collectionId.toLong(),
            artistName = input.artistName,
            collectionName = input.collectionName,
            artworkUrl600 = input.artworkUrl600,
            trackCount = input.trackCount,
            trackName = input.trackName,
            releaseDate = input.releaseDate,
            primaryGenreName = input.primaryGenreName,
        )
    }

    override fun reverseMap(input: PodcastUiState): PodcastDTO {
        return PodcastDTO(
            artistName = input.artistName,
            collectionName = input.collectionName,
            artworkUrl600 = input.artworkUrl600,
            trackCount = input.trackCount,
            trackName = input.trackName,
            releaseDate = input.releaseDate,
            primaryGenreName = input.primaryGenreName,
            collectionId = input.trackId.toInt(),
            wrapperType = "",
            kind = "",
            country = "",
            artistId = 0,
            artistViewUrl = "",
            feedUrl = "",
            genres = listOf(),
            genreIds = listOf(),
            trackViewUrl = "",
            trackTimeMillis = 0,
            collectionViewUrl = "",
            trackId = 0,
        )
    }
}