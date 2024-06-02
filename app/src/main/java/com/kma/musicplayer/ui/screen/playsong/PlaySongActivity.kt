package com.kma.musicplayer.ui.screen.playsong

import android.os.Bundle
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.ActivityPlaySongBinding
import com.kma.musicplayer.ui.screen.core.BaseActivity

class PlaySongActivity : BaseActivity<ActivityPlaySongBinding>() {
    override fun getContentView(): Int = R.layout.activity_play_song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}