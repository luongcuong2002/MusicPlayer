package com.kma.musicplayer.ui.screen.home

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.FragmentHomeBinding
import com.kma.musicplayer.ui.screen.core.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var adapter: ViewPagerAdapter

    override fun getContentView(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ViewPagerAdapter(requireActivity())
        dataBinding.viewPager.adapter = adapter
        dataBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateNavigationBarUI(position)
            }
        })
        setupListeners()
    }

    private fun setupListeners() {
        dataBinding.tvSongs.setOnClickListener {
            dataBinding.viewPager.currentItem = 0
        }
        dataBinding.tvPlaylists.setOnClickListener {
            dataBinding.viewPager.currentItem = 1
        }
        dataBinding.tvArtists.setOnClickListener {
            dataBinding.viewPager.currentItem = 2
        }
    }

    private fun updateNavigationBarUI(position: Int) {
        when (position) {
            0 -> {
                dataBinding.tvSongs.setTextColor(resources.getColor(R.color.white))
                dataBinding.songsBarHighlight.visibility = View.VISIBLE
                dataBinding.tvPlaylists.setTextColor(resources.getColor(R.color.color_787B82))
                dataBinding.playlistsBarHighlight.visibility = View.INVISIBLE
                dataBinding.tvArtists.setTextColor(resources.getColor(R.color.color_787B82))
                dataBinding.artistsBarHighlight.visibility = View.INVISIBLE
            }
            1 -> {
                dataBinding.tvSongs.setTextColor(resources.getColor(R.color.color_787B82))
                dataBinding.songsBarHighlight.visibility = View.INVISIBLE
                dataBinding.tvPlaylists.setTextColor(resources.getColor(R.color.white))
                dataBinding.playlistsBarHighlight.visibility = View.VISIBLE
                dataBinding.tvArtists.setTextColor(resources.getColor(R.color.color_787B82))
                dataBinding.artistsBarHighlight.visibility = View.INVISIBLE
            }
            else -> {
                dataBinding.tvSongs.setTextColor(resources.getColor(R.color.color_787B82))
                dataBinding.songsBarHighlight.visibility = View.INVISIBLE
                dataBinding.tvPlaylists.setTextColor(resources.getColor(R.color.color_787B82))
                dataBinding.playlistsBarHighlight.visibility = View.INVISIBLE
                dataBinding.tvArtists.setTextColor(resources.getColor(R.color.white))
                dataBinding.artistsBarHighlight.visibility = View.VISIBLE
            }
        }
    }
}