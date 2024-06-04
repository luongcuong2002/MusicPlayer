package com.kma.musicplayer.ui.screen.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.utils.SongManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavouriteViewModel : ViewModel() {
    val songs = mutableListOf<Song>()

    val size = MutableLiveData(0)

    private val favouriteSongDao = AppDatabase.INSTANCE.favouriteSongDao()

    suspend fun fetchFavouriteSongs() = withContext(Dispatchers.IO) {
        songs.clear()
        songs.addAll(favouriteSongDao.getAllFavoriteSongs().map {
            SongManager.getSongById(it.songId)
        }.filterNotNull())
        withContext(Dispatchers.Main) {
            size.value = songs.size
        }
    }

    suspend fun filterHiddenSongs() = withContext(Dispatchers.IO) {
        songs.removeAll {
            AppDatabase.INSTANCE.hiddenSongDao().isHidden(it.id)
        }
        withContext(Dispatchers.Main) {
            size.value = songs.size
        }
    }

    suspend fun filterFavoriteSongs() = withContext(Dispatchers.IO) {
        songs.removeAll {
            !AppDatabase.INSTANCE.favouriteSongDao().isFavourite(it.id)
        }
        withContext(Dispatchers.Main) {
            size.value = songs.size
        }
    }
}