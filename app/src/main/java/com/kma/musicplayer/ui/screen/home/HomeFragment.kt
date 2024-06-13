package com.kma.musicplayer.ui.screen.home

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.FragmentHomeBinding
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.ui.screen.core.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var adapter: ViewPagerAdapter

    override fun getContentView(): Int = R.layout.fragment_home

    override fun onThemeChanged(theme: Theme) {
        binding.tvTitle.setTextColor(resources.getColor(theme.titleTextColorRes))
        binding.divider.setCardBackgroundColor(resources.getColor(theme.dividerColorRes))
        updateNavigationBarUI(binding.viewPager.currentItem, theme.activeTabTextColorRes, theme.inactiveTabTextColorRes)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateNavigationBarUI(position, getThemeViewModel().theme.value!!.activeTabTextColorRes, getThemeViewModel().theme.value!!.inactiveTabTextColorRes)
            }
        })
        setupListeners()
        requestUpdateTheme()
    }

    private fun setupListeners() {
        binding.tvSongs.setOnClickListener {
            binding.viewPager.currentItem = 0
        }
        binding.tvPlaylists.setOnClickListener {
            binding.viewPager.currentItem = 1
        }
        binding.tvArtists.setOnClickListener {
            binding.viewPager.currentItem = 2
        }
    }

    private fun updateNavigationBarUI(position: Int, activeColorRes: Int, inactiveColorRes: Int) {
        when (position) {
            0 -> {
                binding.tvSongs.setTextColor(resources.getColor(activeColorRes))
                binding.songsBarHighlight.visibility = View.VISIBLE
                binding.tvPlaylists.setTextColor(resources.getColor(inactiveColorRes))
                binding.playlistsBarHighlight.visibility = View.INVISIBLE
                binding.tvArtists.setTextColor(resources.getColor(inactiveColorRes))
                binding.artistsBarHighlight.visibility = View.INVISIBLE
            }
            1 -> {
                binding.tvSongs.setTextColor(resources.getColor(inactiveColorRes))
                binding.songsBarHighlight.visibility = View.INVISIBLE
                binding.tvPlaylists.setTextColor(resources.getColor(activeColorRes))
                binding.playlistsBarHighlight.visibility = View.VISIBLE
                binding.tvArtists.setTextColor(resources.getColor(inactiveColorRes))
                binding.artistsBarHighlight.visibility = View.INVISIBLE
            }
            else -> {
                binding.tvSongs.setTextColor(resources.getColor(inactiveColorRes))
                binding.songsBarHighlight.visibility = View.INVISIBLE
                binding.tvPlaylists.setTextColor(resources.getColor(inactiveColorRes))
                binding.playlistsBarHighlight.visibility = View.INVISIBLE
                binding.tvArtists.setTextColor(resources.getColor(activeColorRes))
                binding.artistsBarHighlight.visibility = View.VISIBLE
            }
        }
    }
}