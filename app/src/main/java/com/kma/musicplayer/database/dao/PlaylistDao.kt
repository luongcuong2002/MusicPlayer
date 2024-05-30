package com.kma.musicplayer.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.PlaylistModel
import java.io.File

@Dao
interface PlaylistDao {

    @Query("SELECT COUNT(*) FROM PlaylistModel WHERE playlist_name = :playlistName")
    suspend fun isPlaylistNameExisted(playlistName: String): Boolean

    @Insert
    fun insertPlaylist(playlistModel: PlaylistModel)

    @Update
    fun updateOriginalPlaylist(playlistModel: PlaylistModel)

    @Update
    fun updatePlaylist(playlistModel: PlaylistModel) {
        val originalPlaylistModel = getOriginalPlaylistById(playlistModel.id)
        playlistModel.songIds.addAll(
            originalPlaylistModel.songIds.filter { AppDatabase.INSTANCE.hiddenSongDao().isHidden(it) }
        )
        updateOriginalPlaylist(playlistModel)
    }

    @Query("UPDATE PlaylistModel SET song_ids = :songIds WHERE id = :id")
    fun updateMediaPaths(id: Int, songIds: List<String>)

    @Query("SELECT * FROM PlaylistModel WHERE id = :id")
    fun getOriginalPlaylistById(id: Int): PlaylistModel

    fun getPlaylistById(id: Int): PlaylistModel {
        val playlist = getOriginalPlaylistById(id)
        playlist.songIds.removeAll {
            AppDatabase.INSTANCE.hiddenSongDao().isHidden(it)
        }
        return playlist
    }

    @Delete
    fun deletePlaylist(playlistModel: PlaylistModel)

    @Query("SELECT * FROM PlaylistModel")
    suspend fun getAllOriginalPlaylist(): List<PlaylistModel>

    suspend fun getAllPlaylists(): List<PlaylistModel> {
        val playlists = getAllOriginalPlaylist()
        playlists.forEach {
            it.songIds.removeAll {
                AppDatabase.INSTANCE.hiddenSongDao().isHidden(it)
            }
        }
        return playlists
    }
}