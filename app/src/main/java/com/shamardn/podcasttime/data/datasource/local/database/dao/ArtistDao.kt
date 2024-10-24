package com.shamardn.podcasttime.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.shamardn.podcasttime.data.datasource.local.database.entity.ArtistEntity

@Dao
interface ArtistDao {
    @Query("SELECT * FROM ARTIST_TABLE WHERE artistName LIKE '%' || :text || '%'")
    suspend fun getArtistsByName(text: String): List<ArtistEntity>

    @Query("SELECT * FROM ARTIST_TABLE ORDER BY artistName")
    suspend fun getArtistsListOrderedByName(): List<ArtistEntity>

    @Query("SELECT * FROM ARTIST_TABLE ORDER BY collectionName")
    suspend fun getArtistsListOrderedByPodcast(): List<ArtistEntity>

    @Delete
    suspend fun deleteArtist(artist: ArtistEntity)

    @Query("DELETE FROM ARTIST_TABLE")
    suspend fun deleteArtistList()
}