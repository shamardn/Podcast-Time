package com.shamardn.podcasttime.ui.common.mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class PodcastUiStateMapper @Inject constructor(

) : Mapper<PodcastEntity, PodcastUiState>() {
    override fun map(input: PodcastEntity): PodcastUiState {
        return PodcastUiState(
            trackId = input.trackId,
            trackName = input.trackName,
            artworkUrl100 = input.artworkUrl100,
            releaseDate = input.releaseDate,
            trackCount = input.trackCount,
            primaryGenreName = input.primaryGenreName,
            artistName = input.artistName,
            collectionName = input.collectionName,
        )
    }

    companion object {
        fun PodcastUiState.toEntity(): PodcastEntity {
            return PodcastEntity(
                trackId = trackId,
                trackName = trackName,
                artworkUrl100 = artworkUrl100,
                trackCount = trackCount,
                artistName = artistName,
                primaryGenreName = primaryGenreName,
                releaseDate = releaseDate,
                collectionName = collectionName,
            )
        }
    }
}