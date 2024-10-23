package com.shamardn.podcasttime.domain.mapper
//
//import javax.inject.Inject
//
//class EpisodeAudioMapper @Inject constructor(
//
//)
//    : Mapper<EpisodeDTO, EpisodeAudio>()
//{
//    override fun map(input: EpisodeDTO): EpisodeAudio {
//        return EpisodeAudio(
//            id = input.trackId,
//            collectionName = input.collectionName,
//            trackName = input.trackName,
//            releaseDate = input.releaseDate,
//            description = input.description,
//            trackTimeMillis = input.trackTimeMillis,
//            artworkUrl160 = input.artworkUrl160,
//            episodeUrl = input.episodeUrl,
//            guid = input.episodeGuid,
//            episodeFileExtension = input.episodeFileExtension ?: "",
//        )
//    }
//}