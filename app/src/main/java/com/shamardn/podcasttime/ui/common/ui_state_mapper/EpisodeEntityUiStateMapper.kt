package com.shamardn.podcasttime.ui.common.ui_state_mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import javax.inject.Inject

class EpisodeEntityUiStateMapper @Inject constructor(

): Mapper<EpisodeEntity, EpisodeUiState>() {
    override fun map(input: EpisodeEntity): EpisodeUiState {
        return EpisodeUiState(
            id = input.collectionId.toLong(),
            trackName = input.trackName,
            artworkUrl600 = input.artworkUrl600,
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

    override fun reverseMap(input: EpisodeUiState): EpisodeEntity {
        return EpisodeEntity(
            guid = input.guid,
            artistName = input.artistName,
            id = input.id,
            trackName = input.trackName,
            collectionId = input.collectionId,
            collectionName = input.collectionName,
            releaseDate = input.releaseDate,
            description = input.description,
            artworkUrl600 = input.artworkUrl600,
            episodeUrl = input.episodeUrl,
            episodeFileExtension = input.episodeFileExtension,
            trackTimeMillis = input.trackTimeMillis,
        )
    }
}