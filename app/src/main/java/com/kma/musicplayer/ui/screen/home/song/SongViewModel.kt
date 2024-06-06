package com.kma.musicplayer.ui.screen.home.song

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.network.common.ApiCallState
import com.kma.musicplayer.utils.SongManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongViewModel : ViewModel() {

    val songs = mutableListOf<OnlineSong>()

    private val _apiCallState = MutableLiveData(ApiCallState.LOADING)
    val apiCallState: LiveData<ApiCallState> = _apiCallState

    fun fetAllSongs() {
        _apiCallState.value = ApiCallState.LOADING
        SongManager.fetchAllOnlineSong(
            onSuccess = {
                CoroutineScope(Dispatchers.Main).launch {
                    songs.clear()
                    songs.apply {
                        addAll(it)
                    }
                    filterHiddenSongs()
                    _apiCallState.value = ApiCallState.SUCCESS
                }
            },
            onError = {
                _apiCallState.value = ApiCallState.ERROR
            }
        )
    }

    suspend fun filterHiddenSongs() = withContext(Dispatchers.IO) {
        songs.removeAll {
            AppDatabase.INSTANCE.hiddenSongDao().isHidden(it.id)
        }
    }
}