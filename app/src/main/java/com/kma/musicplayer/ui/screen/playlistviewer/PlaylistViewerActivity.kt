package com.kma.musicplayer.ui.screen.playlistviewer

import android.os.Bundle
import com.kma.musicplayer.databinding.ActivityPlaylistViewerBinding
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.ui.screen.core.BaseActivity
import com.kma.musicplayer.R
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.PlaylistModel
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.ui.bottomsheet.add_to_playlist.AddToPlaylistBottomSheet
import com.kma.musicplayer.ui.bottomsheet.song_option.SongOptionBottomSheet
import com.kma.musicplayer.ui.screen.home.song.SongAdapter
import com.kma.musicplayer.ui.screen.playsong.PlaySongActivity
import com.kma.musicplayer.utils.Constant
import com.kma.musicplayer.utils.ShareUtils
import com.kma.musicplayer.utils.SongManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class PlaylistViewerActivity : BaseActivity<ActivityPlaylistViewerBinding>() {

    private val songs = mutableListOf<Song>()
    private var songAdapter: SongAdapter? = null

    override fun getContentView(): Int = R.layout.activity_playlist_viewer

    override fun onThemeChanged(theme: Theme) {
        binding.root.setBackgroundColor(resources.getColor(theme.backgroundColorRes))
        binding.backButton.setImageResource(theme.imageBackButtonRes)
        binding.tvTitle.setTextColor(resources.getColor(theme.titleTextColorRes))
        binding.tvAllSongs.setTextColor(resources.getColor(theme.adapterTitleTextColorRes))
        songAdapter?.setTheme(theme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playlist = intent.getSerializableExtra(Constant.BUNDLE_PLAYLIST) as PlaylistModel
        binding.tvTitle.text = playlist.playlistName
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val playlist = intent.getSerializableExtra(Constant.BUNDLE_PLAYLIST) as PlaylistModel

        songs.addAll(
            playlist.songIds.mapNotNull { SongManager.getSongById(it) }
        )

        songAdapter = SongAdapter(
            songs = songs,
            onMoreClick = { song ->
                var bottomSheet: SongOptionBottomSheet? = null
                bottomSheet = SongOptionBottomSheet(
                    song = song,
                    showDeleteFromPlaylist = true,
                    onClickPlayNext = {
                        songService?.songs?.add(song)
                        bottomSheet?.dismiss()
                    },
                    onClickAddToPlaylist = {
                        val addToPlaylistBottomSheet = AddToPlaylistBottomSheet(listOf(song.id))
                        addToPlaylistBottomSheet.show(
                            supportFragmentManager,
                            addToPlaylistBottomSheet.tag
                        )
                        bottomSheet?.dismiss()
                    },
                    onClickHide = {
                        CoroutineScope(Dispatchers.Main).launch {
                            AppDatabase.INSTANCE.hiddenSongDao().insert(song.id)
                            songs.removeAll { AppDatabase.INSTANCE.hiddenSongDao().isHidden(it.id) }
                            songAdapter?.notifyDataSetChanged()
                            bottomSheet?.dismiss()

                            if (songs.isEmpty()) {
                                finish()
                            }
                        }
                    },
                    onClickShare = {
                        ShareUtils.shareSong(this, song)
                        bottomSheet?.dismiss()
                    },
                    onDeleteFromPlaylist = {
                        CoroutineScope(Dispatchers.Main).launch {
                            AppDatabase.INSTANCE.playlistDao().deleteSongFromPlaylist(playlist.id, song.id)
                            songs.remove(song)
                            songAdapter?.notifyDataSetChanged()
                            bottomSheet?.dismiss()

                            if (songs.isEmpty()) {
                                finish()
                            }
                        }
                    },
                )
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            },
            onSongClick = { song ->
                showActivity(
                    PlaySongActivity::class.java,
                    Bundle().apply {
                        putSerializable(Constant.BUNDLE_SONGS, mutableListOf(song) as Serializable)
                    },
                )
            },
        )
        binding.rvSongs.adapter = songAdapter
    }
}