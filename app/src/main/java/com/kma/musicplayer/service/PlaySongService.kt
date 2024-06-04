package com.kma.musicplayer.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.kma.musicplayer.model.RepeatMode
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.utils.AudioPlayerManager

class PlaySongService : Service() {

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): PlaySongService {
            return this@PlaySongService
        }
    }

    override fun onBind(p0: Intent?): IBinder = binder

    var songs: MutableList<Song> = mutableListOf()
    var currentIndex: Int = 0
    var playingSong = MutableLiveData<Song>()
    var isPlayRandomlyEnabled = MutableLiveData<Boolean>().apply {
        value = false
    }
    var repeatMode = MutableLiveData<RepeatMode>().apply {
        value = RepeatMode.NONE
    }
    var isAbleToNext = MutableLiveData<Boolean>().apply {
        value = false
    }
    var isAbleToPrevious = MutableLiveData<Boolean>().apply {
        value = false
    }

    private var _audioPlayerManager: AudioPlayerManager? = null
    val audioPlayerManager: AudioPlayerManager?
        get() = _audioPlayerManager

    override fun onCreate() {
        super.onCreate()
        _audioPlayerManager = AudioPlayerManager(this, songs)
    }

    fun playNext() {
        if (isPlayRandomlyEnabled.value == true) {
            val randomIndex = (0 until songs.size).random()
            updateCurrentIndexValue(randomIndex)
        } else {
            updateCurrentIndexValue(currentIndex + 1)
        }
        playAt(currentIndex)
    }

    fun playPrevious() {
        updateCurrentIndexValue(currentIndex - 1)
        playAt(currentIndex)
    }

    fun playAt(index: Int) {
        updateCurrentIndexValue(index)
        checkAbleToNext()
        checkAbleToPrevious()
        _audioPlayerManager?.play(currentIndex)
    }

    fun setPlayRandomlyEnabled(isEnabled: Boolean) {
        isPlayRandomlyEnabled.value = isEnabled
    }

    fun setRepeatMode(mode: RepeatMode) {
        repeatMode.value = mode
    }

    private fun updateCurrentIndexValue(index: Int) {
        currentIndex = index
        playingSong.value = songs[currentIndex]
    }

    private fun checkAbleToNext() {
        isAbleToNext.value = currentIndex < songs.size - 1
    }

    private fun checkAbleToPrevious() {
        isAbleToPrevious.value = currentIndex > 0
    }

    override fun onDestroy() {
        super.onDestroy()
        _audioPlayerManager?.releasePlayer()
    }
}