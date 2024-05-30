package com.kma.musicplayer.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kma.musicplayer.model.PlaylistModel
import java.io.File

@Dao
interface PlaylistDao {

    @Query("SELECT COUNT(*) FROM PlaylistModel WHERE playlist_name = :playlistName")
    suspend fun isPlaylistNameExisted(playlistName: String): Boolean

    @Insert
    fun insertPlaylist(playlistModel: PlaylistModel)

    @Update
    fun updatePlaylist(playlistModel: PlaylistModel)

    @Query("UPDATE PlaylistModel SET song_ids = :songIds WHERE id = :id")
    fun updateMediaPaths(id: Int, songIds: List<String>)

    @Query("SELECT * FROM PlaylistModel WHERE id = :id")
    fun getPlaylistById(id: Int): PlaylistModel

    @Delete
    fun deletePlaylist(playlistModel: PlaylistModel)

    @Query("SELECT * FROM PlaylistModel")
    suspend fun getAllPlaylists(): List<PlaylistModel>
}