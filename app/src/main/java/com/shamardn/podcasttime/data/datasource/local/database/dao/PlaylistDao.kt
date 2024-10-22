package com.shamardn.podcasttime.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shamardn.podcasttime.data.datasource.local.database.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM PLAYLIST_TABLE")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM PLAYLIST_TABLE WHERE playlistName = :name ")
    fun getPlaylistByName(name: String): PlaylistEntity

}
