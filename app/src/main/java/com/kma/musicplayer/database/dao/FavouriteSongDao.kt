package com.kma.musicplayer.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface FavouriteSongDao {
    @Query("SELECT COUNT(*) FROM FavouriteSong WHERE song_id = :songId")
    fun isFavourite(songId: String): Boolean

    @Query("INSERT INTO FavouriteSong (song_id) VALUES (:songId)")
    fun insert(songId: String)

    @Query("DELETE FROM FavouriteSong WHERE song_id = :songId")
    fun delete(songId: String)
}