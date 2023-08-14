package com.shamardn.podcasttime.domain.mapper

import com.shamardn.podcasttime.data.local.database.entity.EpisodeAudio
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import javax.inject.Inject

class EpisodeAudioMapper @Inject constructor(

) : Mapper<EpisodeDTO,EpisodeAudio>(){
    override fun map(input: EpisodeDTO): EpisodeAudio {
        return EpisodeAudio(
            id = input.trackId,
            collectionName = input.collectionName,
            trackName = input.trackName,
            releaseDate = input.releaseDate,
            description = input.description ?: "",
            trackTimeMillis = input.trackTimeMillis,
            artworkUrl160 = input.artworkUrl160 ?: input.artworkUrl100,
            episodeUrl = input.episodeUrl ?: input.collectionViewUrl,
            guid = input.episodeGuid ?: "",
            episodeFileExtension = input.episodeFileExtension ?: "",
        )
    }
}