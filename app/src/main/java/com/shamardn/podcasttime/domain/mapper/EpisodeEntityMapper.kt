package com.shamardn.podcasttime.domain.mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeAudio
import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import javax.inject.Inject

class EpisodeEntityMapper @Inject constructor(

) : Mapper<EpisodeEntity, EpisodeAudio>(){
    override fun map(input: EpisodeEntity): EpisodeAudio {
        return EpisodeAudio(
            id = input.id,
            collectionName = input.collectionName,
            trackName = input.trackName,
            releaseDate = input.releaseDate,
            description = input.description,
            trackTimeMillis = input.trackTimeMillis,
            artworkUrl160 = input.artworkUrl160,
            episodeUrl = input.episodeUrl,
            episodeFileExtension = input.episodeFileExtension,
            guid = input.guid,
        )
    }
}