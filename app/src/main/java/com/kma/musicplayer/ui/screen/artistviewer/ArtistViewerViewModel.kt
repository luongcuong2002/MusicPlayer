package com.kma.musicplayer.ui.screen.artistviewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.Artist
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.utils.SongManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArtistViewerViewModel : ViewModel() {
    val songs = mutableListOf<Song>()

    val size = MutableLiveData(0)

    fun fetchSongs(artist: Artist) {
        songs.clear()
        songs.addAll(
            SongManager.getSongByArtist(artist)
        )
        size.value = songs.size
    }

    suspend fun filterHiddenSongs() = withContext(Dispatchers.IO) {
        songs.removeAll {
            AppDatabase.INSTANCE.hiddenSongDao().isHidden(it.id)
        }
        withContext(Dispatchers.Main) {
            size.value = songs.size
        }
    }
}