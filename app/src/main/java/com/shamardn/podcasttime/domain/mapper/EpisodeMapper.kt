package com.shamardn.podcasttime.domain.mapper

import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import javax.inject.Inject

class EpisodeMapper @Inject constructor(

) : Mapper<EpisodeDTO,EpisodeEntity>(){
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
        )
    }
}