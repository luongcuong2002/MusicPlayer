package com.kma.musicplayer.ui.screen.musiclocal

import android.os.Bundle
import android.view.View
import com.kma.musicplayer.databinding.FragmentMusicLocalBinding
import com.kma.musicplayer.ui.screen.core.BaseFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.model.Theme

class MusicLocalFragment : BaseFragment<FragmentMusicLocalBinding>() {

    override fun getContentView(): Int = R.layout.fragment_music_local

    override fun onThemeChanged(theme: Theme) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getThemeViewModel().theme.observe(viewLifecycleOwner) {
            onThemeChanged(it)
        }
    }
}