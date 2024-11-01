package com.shamardn.podcasttime.domain.mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.data.datasource.remote.dtos.PodcastDTO
import javax.inject.Inject

class PodcastEntityDTOMapper @Inject constructor(
) : Mapper<PodcastDTO, PodcastEntity>(){
    override fun map(input: PodcastDTO): PodcastEntity {
        return PodcastEntity(
            trackId = input.collectionId.toLong(),
            artistName = input.artistName,
            collectionName = input.collectionName,
            artworkUrl600 = input.artworkUrl600,
            trackCount = input.trackCount,
            trackName = input.trackName,
            releaseDate = input.releaseDate,
            primaryGenreName = input.primaryGenreName,
        )
    }

    override fun reverseMap(input: PodcastEntity): PodcastDTO {
        return PodcastDTO(
            artistName = input.artistName,
            collectionName = input.collectionName,
            artworkUrl600 = input.artworkUrl600,
            trackCount = input.trackCount,
            trackName = input.trackName,
            releaseDate = input.releaseDate,
            primaryGenreName = input.primaryGenreName,
            collectionId = input.trackId.toInt(),
            wrapperType = "",
            kind = "",
            country = "",
            artistId = 0,
            artistViewUrl = "",
            feedUrl = "",
            genres = listOf(),
            genreIds = listOf(),
            trackViewUrl = "",
            trackTimeMillis = 0,
            collectionViewUrl = "",
            trackId = 0,
        )
    }
}