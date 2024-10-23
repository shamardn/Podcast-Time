package com.shamardn.podcasttime.ui.download

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeDownloadEntity
import com.shamardn.podcasttime.domain.mapper.Mapper
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import javax.inject.Inject

class DownloadEpisodeMapper @Inject constructor(

): Mapper<EpisodeDownloadEntity, EpisodeUiState>() {
    override fun map(input: EpisodeDownloadEntity): EpisodeUiState {
        return EpisodeUiState(
            id = input.id,
            trackName = input.trackName,
            artworkUrl100 = input.artworkUrl100,
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

    companion object {
        fun toEpisodeDownloadEntity(episode: EpisodeUiState): EpisodeDownloadEntity {
            return EpisodeDownloadEntity(
                id = episode.id,
                trackName = episode.trackName,
                artworkUrl100 = episode.artworkUrl100,
                artistName = episode.artistName,
                releaseDate = episode.releaseDate,
                collectionName = episode.collectionName,
                episodeUrl = episode.episodeUrl,
                episodeFileExtension = episode.episodeFileExtension,
                description = episode.description,
                trackTimeMillis = episode.trackTimeMillis,
                collectionId = episode.collectionId,
                guid = episode.guid,
            )
        }

    }
}