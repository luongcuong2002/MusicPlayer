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

    private fun setPlayVideo(song: Song) {
        simpleExoPlayer?.stop()
        val mediaItem = MediaItem.fromUri(song.path)
        simpleExoPlayer?.setMediaItem(mediaItem)
        simpleExoPlayer?.prepare()
        simpleExoPlayer?.playWhenReady = true
    }

    fun releasePlayer() {
        simpleExoPlayer?.release()
    }
}