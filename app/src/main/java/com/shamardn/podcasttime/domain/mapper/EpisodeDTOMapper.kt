package com.shamardn.podcasttime.domain.mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.data.datasource.remote.dtos.EpisodeDTO
import javax.inject.Inject

class EpisodeDTOMapper @Inject constructor(

) : Mapper<EpisodeDTO, EpisodeEntity>(){
    override fun map(input: EpisodeDTO): EpisodeEntity {
        return EpisodeEntity(
            id = input.trackId ?: 0,
            collectionName = input.collectionName ?: "collectionName is null",
            trackName = input.trackName ?: "trackName is null",
            releaseDate = input.releaseDate ?: "1970-10-12T12:46:00Z",
            description = input.shortDescription ?: "description is null",
            trackTimeMillis = input.trackTimeMillis ?: 0,
            artworkUrl100 = input.artworkUrl100 ?: input.artworkUrl160 ?: "artworkUrl is null",
            episodeUrl = input.episodeUrl ?: "episodeUrl is null",
            guid = input.episodeGuid ?: "guid is null",
            episodeFileExtension = input.episodeFileExtension ?: "mp3",
            collectionId = input.collectionId ?: 0,
            artistName = input.artistName ?: "artistName is null",
        )
    }
}