package com.kma.musicplayer.ui.bottomsheet.add_to_playlist

import com.kma.musicplayer.model.PlaylistModel

data class SelectablePlaylist(
    val playlistModel: PlaylistModel,
    var isSelected: Boolean = false
)