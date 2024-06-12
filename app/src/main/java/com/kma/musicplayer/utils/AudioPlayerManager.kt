package com.kma.musicplayer.utils

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.kma.musicplayer.model.Song

class AudioPlayerManager(
    private val context: Context,
    private val songs: MutableList<Song>
) {
    var simpleExoPlayer: ExoPlayer? = null

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        simpleExoPlayer = ExoPlayer.Builder(context).build()
    }

    fun play(posSelect: Int) {
        val currentVideo = songs[posSelect]
        setPlayVideo(currentVideo)
    }

    fun setPlayerAt(posSelect: Int) {
        val currentVideo = songs[posSelect]
        setPlayVideo(currentVideo, false)
    }

    private fun setPlayVideo(song: Song, isPlay: Boolean = true) {
        simpleExoPlayer?.stop()
        val mediaItem = MediaItem.fromUri(song.path)
        simpleExoPlayer?.setMediaItem(mediaItem)
        simpleExoPlayer?.prepare()
        simpleExoPlayer?.playWhenReady = isPlay
    }

    fun releasePlayer() {
        simpleExoPlayer?.release()
    }
}