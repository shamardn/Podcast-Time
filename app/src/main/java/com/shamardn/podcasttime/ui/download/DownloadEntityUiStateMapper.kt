package com.shamardn.podcasttime.ui.download

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeDownloadEntity
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import javax.inject.Inject

class DownloadEntityUiStateMapper @Inject constructor(

): Mapper<EpisodeDownloadEntity, EpisodeUiState>() {
    override fun map(input: EpisodeDownloadEntity): EpisodeUiState {
        return EpisodeUiState(
            id = input.id,
            trackName = input.trackName,
            artworkUrl600 = input.artworkUrl600,
            artistName = input.artistName,
            releaseDate = input.releaseDate,
            collectionName = input.collectionName,
            episodeUrl = input.episodeUrl,
            episodeFileExtension = input.episodeFileExtension,
            description = input.description,
            trackTimeMillis = input.trackTimeMillis,
            collectionId = input.collectionId,
            guid = input.guid,
        )
    }

    override fun reverseMap(input: EpisodeUiState): EpisodeDownloadEntity {
        return EpisodeDownloadEntity(
            id = input.id,
            trackName = input.trackName,
            artworkUrl600 = input.artworkUrl600,
            artistName = input.artistName,
            releaseDate = input.releaseDate,
            collectionName = input.collectionName,
            episodeUrl = input.episodeUrl,
            episodeFileExtension = input.episodeFileExtension,
            description = input.description,
            trackTimeMillis = input.trackTimeMillis,
            collectionId = input.collectionId,
            guid = input.guid,
        )
    }
}