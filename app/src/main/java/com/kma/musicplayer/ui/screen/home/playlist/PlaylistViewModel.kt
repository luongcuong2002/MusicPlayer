package com.kma.musicplayer.ui.screen.home.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.PlaylistModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistViewModel : ViewModel() {
    val playlists = mutableListOf<PlaylistModel>()

    private val _isNoPlaylist = MutableLiveData(false)
    val isNoPlaylist: LiveData<Boolean> = _isNoPlaylist

    suspend fun getAllPlaylists() {
        playlists.clear()
        playlists.addAll(AppDatabase.INSTANCE.playlistDao().getAllPlaylists())
        withContext(Dispatchers.Main) {
            _isNoPlaylist.value = playlists.isEmpty()
        }
    }
}