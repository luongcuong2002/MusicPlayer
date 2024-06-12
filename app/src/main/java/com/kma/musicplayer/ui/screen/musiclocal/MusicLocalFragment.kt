package com.kma.musicplayer.ui.screen.musiclocal

import android.os.Bundle
import android.view.View
import com.kma.musicplayer.databinding.FragmentMusicLocalBinding
import com.kma.musicplayer.ui.screen.core.BaseFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.ui.screen.home.song.SongAdapter
import com.kma.musicplayer.ui.screen.playsong.PlaySongActivity
import com.kma.musicplayer.utils.Constant
import com.kma.musicplayer.utils.FileUtils
import com.kma.musicplayer.utils.PermissionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class MusicLocalFragment : BaseFragment<FragmentMusicLocalBinding>() {

    private companion object {
        const val REQUEST_CODE_PERMISSION = 100
    }

    private val songs = mutableListOf<Song>()
    private var songAdapter: SongAdapter? = null

    override fun getContentView(): Int = R.layout.fragment_music_local

    override fun onThemeChanged(theme: Theme) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        checkPermissionAndSetupRecycleView()
        getThemeViewModel().theme.observe(viewLifecycleOwner) {
            onThemeChanged(it)
        }
    }

    private fun setupListeners() {
        binding.btnGrantPermission.setOnClickListener {
            PermissionUtils.requestReadAudioPermission(requireActivity(), REQUEST_CODE_PERMISSION)
        }
    }

    private fun setupRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                binding.rvSongs.visibility = View.GONE
                binding.pbLoading.visibility = View.VISIBLE
            }
            songs.addAll(
                FileUtils.getAllAudios(requireContext())
            )
            withContext(Dispatchers.Main) {
                binding.pbLoading.visibility = View.GONE
                binding.rvSongs.visibility = View.VISIBLE

                songAdapter = SongAdapter(
                    songs,
                    onMoreClick = {

                    },
                    onSongClick = {
                        showActivity(
                            PlaySongActivity::class.java,
                            Bundle().apply {
                                putSerializable(Constant.BUNDLE_SONGS, mutableListOf(it) as Serializable)
                            },
                        )
                    }
                )
                binding.rvSongs.adapter = songAdapter
                binding.tvTotalSongs.text = if (songs.size > 1) {
                    "${songs.size} ${getString(R.string.audios)}"
                } else {
                    "${songs.size} ${getString(R.string.audio)}"
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (PermissionUtils.isReadAudioPermissionGranted(requireContext())) {
            binding.llPermission.visibility = View.GONE
            binding.rvSongs.visibility = View.VISIBLE
        } else {
            binding.llPermission.visibility = View.VISIBLE
            binding.rvSongs.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            checkPermissionAndSetupRecycleView()
        }
    }

    private fun checkPermissionAndSetupRecycleView() {
        if (PermissionUtils.isReadAudioPermissionGranted(requireActivity())) {
            setupRecyclerView()
        }
    }
}