package com.kma.musicplayer.screen.songselector

import android.os.Bundle
import com.kma.musicplayer.databinding.ActivitySongSelectorBinding
import com.kma.musicplayer.screen.core.BaseActivity
import com.kma.musicplayer.R
import java.io.BufferedInputStream
import java.io.FileInputStream

class SongSelectorActivity : BaseActivity<ActivitySongSelectorBinding>() {

    override fun getContentView(): Int = R.layout.activity_song_selector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val stream = BufferedInputStream(FileInputStream(""))
    }
}