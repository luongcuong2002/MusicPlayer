package com.kma.musicplayer.ui.screen.home.song

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.network.common.ApiCallState
import com.kma.musicplayer.network.retrofit.model.SongDto
import com.kma.musicplayer.network.retrofit.repository.SongRepository
import com.kma.musicplayer.utils.Mapping
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
            add(
                OnlineSong(
                    1,
                    "Baby",
                    "Justin Bieber",
                    3 * 60 + 10,
                    "https://images.pexels.com/photos/15636411/pexels-photo-15636411/free-photo-of-hoa-trang-tri-cu-c-s-ng-tinh-l-ng-nh.jpeg?auto=compress&cs=tinysrgb&w=600",
                    "1"
                )
            )
            add(
                OnlineSong(
                    2,
                    "See you again",
                    "Charlie Puth",
                    5 * 60 + 12,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8qVLXZ511VApVjnOcB75QtyyTyO-D_3mDXA&s",
                    "1"
                )
            )
            add(
                OnlineSong(
                    3,
                    "Baby",
                    "Justin Bieber",
                    7 * 60 + 21,
                    "https://i.pinimg.com/originals/11/de/d7/11ded77d12eed3ca0204ea387934aeaf.jpg",
                    "1"
                )
            )
            add(
                OnlineSong(
                    4,
                    "See you again",
                    "Charlie Puth",
                    4 * 60 + 13,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8qVLXZ511VApVjnOcB75QtyyTyO-D_3mDXA&s",
                    "1"
                )
            )
            add(
                OnlineSong(
                    5,
                    "Baby",
                    "Justin Bieber",
                    3 * 60 + 10,
                    "https://i.pinimg.com/originals/11/de/d7/11ded77d12eed3ca0204ea387934aeaf.jpg",
                    "1"
                )
            )
            add(
                OnlineSong(
                    6,
                    "See you again",
                    "Charlie Puth",
                    3 * 60 + 10,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8qVLXZ511VApVjnOcB75QtyyTyO-D_3mDXA&s",
                    "1"
                )
            )
            add(
                OnlineSong(
                    7,
                    "Baby",
                    "Justin Bieber",
                    2 * 60 + 46,
                    "https://i.pinimg.com/originals/11/de/d7/11ded77d12eed3ca0204ea387934aeaf.jpg",
                    "1"
                )
            )
            add(
                OnlineSong(
                    8,
                    "See you again",
                    "Charlie Puth",
                    1 * 60 + 65,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8qVLXZ511VApVjnOcB75QtyyTyO-D_3mDXA&s",
                    "1"
                )
            )
        }

        Handler().postDelayed({
            _apiCallState.value = ApiCallState.SUCCESS
        }, 1000)
    }
}