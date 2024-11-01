package com.shamardn.podcasttime.domain.mapper

import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState

@OptIn(UnstableApi::class)
fun toEpisodeEntity(mediaItem: MediaItem): EpisodeUiState {
    val episodeUrl = mediaItem.mediaMetadata.extras?.getString("KEY_EPISODE_PATH") ?: ""
    return EpisodeUiState(
        episodeUrl = episodeUrl,
        guid = mediaItem.mediaId,
        trackName = mediaItem.mediaMetadata.title.toString(),
        collectionName = mediaItem.mediaMetadata.albumTitle.toString(),
        artworkUrl600 = mediaItem.mediaMetadata.artworkUri.toString(),
        description = mediaItem.mediaMetadata.description.toString(),
        releaseDate = mediaItem.mediaMetadata.releaseYear.toString(),
        trackTimeMillis = mediaItem.mediaMetadata.durationMs ?: 0 ,
        id = 0,
        episodeFileExtension = "mp3",
        artistName = mediaItem.mediaMetadata.artist.toString(),
        collectionId = 0,
    )
}