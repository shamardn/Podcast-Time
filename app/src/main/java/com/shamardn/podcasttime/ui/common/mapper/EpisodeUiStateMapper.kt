package com.shamardn.podcasttime.ui.common.mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import javax.inject.Inject

class EpisodeUiStateMapper @Inject constructor(

): Mapper<EpisodeEntity, EpisodeUiState>() {
    override fun map(input: EpisodeEntity): EpisodeUiState {
        return EpisodeUiState(
            id = input.collectionId.toLong(),
            trackName = input.trackName,
            artworkUrl100 = input.artworkUrl100,
            artistName = input.artistName,
            releaseDate = input.releaseDate,
            collectionName = input.collectionName,
            episodeUrl = input.episodeUrl,
            episodeFileExtension = input.episodeFileExtension,
            description = input.description,
            guid = input.guid,
            trackTimeMillis = input.trackTimeMillis,
            collectionId = input.collectionId,
        )
    }
}