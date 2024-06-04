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
    var isPlaying = MutableLiveData<Boolean>().apply {
        value = false
    }

    private var _audioPlayerManager: AudioPlayerManager? = null
    val audioPlayerManager: AudioPlayerManager?
        get() = _audioPlayerManager

    override fun onCreate() {
        super.onCreate()
        _audioPlayerManager = AudioPlayerManager(this, songs)
    }

    fun pause() {
        _audioPlayerManager?.simpleExoPlayer?.pause()
        isPlaying.value = false
    }

    fun resume() {
        _audioPlayerManager?.simpleExoPlayer?.play()
        isPlaying.value = true
    }

    fun addMore(songs: List<Song>) {
        this.songs.addAll(songs)
    }

    fun playNext() {
        if (isPlayRandomlyEnabled.value == true) {
            val randomIndex = (0 until songs.size).random()
            updateCurrentIndexValue(randomIndex)
        } else {
            val nextIndex = if (currentIndex == songs.size - 1) 0 else currentIndex + 1
            updateCurrentIndexValue(nextIndex)
        }
        playAt(currentIndex)
    }

    fun playPrevious() {
        val index = if (currentIndex == 0) songs.size - 1 else currentIndex - 1
        updateCurrentIndexValue(index)
        playAt(currentIndex)
    }

    fun playAt(index: Int) {
        updateCurrentIndexValue(index)
        _audioPlayerManager?.play(currentIndex)
        isPlaying.value = true
    }

    fun setPlayRandomlyEnabled(isEnabled: Boolean) {
        isPlayRandomlyEnabled.value = isEnabled
    }

    fun setRepeatMode(mode: RepeatMode) {
        repeatMode.value = mode
    }

    fun changeCurrentIndex(index: Int) {
        currentIndex = index
    }

    private fun updateCurrentIndexValue(index: Int) {
        currentIndex = index
        playingSong.value = songs[currentIndex]
    }

    override fun onDestroy() {
        super.onDestroy()
        _audioPlayerManager?.releasePlayer()
    }
}