package com.shamardn.podcasttime.domain.mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import javax.inject.Inject

class EpisodeDTOMapper @Inject constructor(

) : Mapper<EpisodeDTO, EpisodeEntity>(){
    override fun map(input: EpisodeDTO): EpisodeEntity {
        return EpisodeEntity(
            id = input.trackId,
            collectionName = input.collectionName,
            trackName = input.trackName,
            releaseDate = input.releaseDate,
            description = input.description,
            trackTimeMillis = input.trackTimeMillis,
            artworkUrl160 = input.artworkUrl160,
            episodeUrl = input.episodeUrl,
            guid = input.episodeGuid,
            episodeFileExtension = input.episodeFileExtension,
        )
    }
}