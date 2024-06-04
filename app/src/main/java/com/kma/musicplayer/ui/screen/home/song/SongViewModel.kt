package com.kma.musicplayer.ui.screen.home.song

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.network.common.ApiCallState
import com.kma.musicplayer.network.retrofit.model.SongDto
import com.kma.musicplayer.network.retrofit.repository.SongRepository
import com.kma.musicplayer.utils.Mapping
import com.kma.musicplayer.utils.SongManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SongViewModel : ViewModel() {

    val songs = mutableListOf<OnlineSong>()

    private val _apiCallState = MutableLiveData(ApiCallState.LOADING)
    val apiCallState: LiveData<ApiCallState> = _apiCallState

    init {
        fetAllSongs()
    }

    fun fetAllSongs() {
        _apiCallState.value = ApiCallState.LOADING
//        SongRepository.getAllSongs(object : Callback<List<SongDto>> {
//            override fun onResponse(call: Call<List<SongDto>>, response: Response<List<SongDto>>) {
//                songs.clear()
//                songs.addAll(
//                    response.body()!!.map {
//                        Mapping.mapToOnlineSong(it)
//                    },
//                )
//                _apiCallState.value = ApiCallState.SUCCESS
//            }
//
//            override fun onFailure(call: Call<List<SongDto>>, t: Throwable) {
//                _apiCallState.value = ApiCallState.ERROR
//            }
//        })
        songs.apply {
            addAll(SongManager.getAllOnlineSong())
        }

        CoroutineScope(Dispatchers.Main).launch {
            filterHiddenSongs()
            Handler().postDelayed({
                _apiCallState.value = ApiCallState.SUCCESS
            }, 1000)
        }
    }

    suspend fun filterHiddenSongs() = withContext(Dispatchers.IO) {
        songs.removeAll {
            AppDatabase.INSTANCE.hiddenSongDao().isHidden(it.id)
        }
    }
}