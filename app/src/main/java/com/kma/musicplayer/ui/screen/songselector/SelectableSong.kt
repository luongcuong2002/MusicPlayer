package com.kma.musicplayer.ui.screen.songselector

import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Song

data class SelectableSong(
    val song: Song,
    var isSelected: Boolean = false
)
