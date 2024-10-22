package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.datasource.local.database.entity.PlaylistEntity
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo

class GetFavouritePlaylistUseCase(
    private val repo: PodcastRepo,
) {
    suspend operator fun invoke(): PlaylistEntity {
        return repo.getFavouritePlaylist()
    }
}