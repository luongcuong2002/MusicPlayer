package com.kma.musicplayer.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.FavouriteSong
import com.kma.musicplayer.model.PlaylistModel

@Dao
interface FavouriteSongDao {
    @Query("SELECT COUNT(*) FROM FavouriteSong WHERE song_id = :songId")
    fun isFavourite(songId: String): Boolean

    @Query("INSERT INTO FavouriteSong (song_id) VALUES (:songId)")
    fun insert(songId: String)

    @Query("DELETE FROM FavouriteSong WHERE song_id = :songId")
    fun delete(songId: String)

    @Query("SELECT * FROM FavouriteSong")
    fun getOriginalFavouriteSongs(): List<FavouriteSong>

    fun getAllFavoriteSongs(): List<FavouriteSong> {
        val songs = getOriginalFavouriteSongs().toMutableList()
        songs.removeAll {
            AppDatabase.INSTANCE.hiddenSongDao().isHidden(it.songId)
        }
        return songs
    }
}