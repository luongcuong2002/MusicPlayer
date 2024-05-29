package com.kma.musicplayer.screen.songselector

import com.kma.musicplayer.model.OnlineSong

data class SelectableSong(
    val song: OnlineSong,
    var isSelected: Boolean = false
)
