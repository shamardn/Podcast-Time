package com.shamardn.podcasttime.domain.mapper

import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.data.datasource.remote.dtos.PodcastDTO
import javax.inject.Inject

class PodcastDTOMapper @Inject constructor(

) : Mapper<PodcastDTO, PodcastEntity>(){
    override fun map(input: PodcastDTO): PodcastEntity {
        return PodcastEntity(
            trackId = input.collectionId.toLong(),
            artistName = input.artistName,
            collectionName = input.collectionName,
            artworkUrl100 = input.artworkUrl600,
            trackCount = input.trackCount,
            trackName = input.trackName,
            releaseDate = input.releaseDate,
            primaryGenreName = input.primaryGenreName,
        )
    }
}