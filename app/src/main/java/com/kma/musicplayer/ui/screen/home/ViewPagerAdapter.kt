package com.kma.musicplayer.ui.screen.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kma.musicplayer.ui.screen.home.artist.ArtistFragment
import com.kma.musicplayer.ui.screen.home.playlist.PlaylistFragment
import com.kma.musicplayer.ui.screen.home.song.SongFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SongFragment()
            1 -> PlaylistFragment()
            else -> ArtistFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}
