package com.kma.musicplayer.ui.screen.home.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayer.model.Artist
import com.kma.musicplayer.utils.SongManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArtistViewModel : ViewModel() {
    val artists = mutableListOf<Artist>()

    private val _isNoPlaylist = MutableLiveData(false)
    val isNoPlaylist: LiveData<Boolean> = _isNoPlaylist

    suspend fun getAllArtist() {
        artists.clear()
        artists.addAll(SongManager.getAllArtist())
        withContext(Dispatchers.Main) {
            _isNoPlaylist.value = artists.isEmpty()
        }
    }
}