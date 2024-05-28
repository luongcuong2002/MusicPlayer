package com.kma.musicplayer.screen.home

import android.os.Bundle
import android.view.View
import com.kma.musicplayer.databinding.FragmentHomeBinding
import com.kma.musicplayer.screen.core.BaseFragment
import com.kma.musicplayer.R

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun getContentView(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }
}