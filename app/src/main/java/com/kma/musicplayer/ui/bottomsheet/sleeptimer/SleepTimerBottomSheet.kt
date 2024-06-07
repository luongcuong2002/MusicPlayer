package com.kma.musicplayer.ui.bottomsheet.sleeptimer

import android.annotation.SuppressLint
import android.app.Dialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.BottomSheetSleepTimerBinding
import com.kma.musicplayer.databinding.LayoutItemSleepTimerBinding
import com.kma.musicplayer.model.SleepTimerModel
import com.kma.musicplayer.service.PlaySongService
import com.kma.musicplayer.ui.bottomsheet.customsleeptimer.CustomSleepTimerBottomSheet
import com.kma.musicplayer.utils.Formatter

class SleepTimerBottomSheet(
    private val supportFragmentManager: androidx.fragment.app.FragmentManager,
    private val songService: PlaySongService
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSleepTimerBinding

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = BottomSheetSleepTimerBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        initView()
        setupListeners()
        setupObservers()
    }

    private fun initView() {
        SleepTimerModel.entries.forEach { model ->
            val itemBinding = LayoutItemSleepTimerBinding.inflate(layoutInflater)

            itemBinding.tvTime.text = getString(model.textRes)
            itemBinding.root.setOnClickListener {
                when (model) {
                    SleepTimerModel.CUSTOM -> {
                        val customSleepTimerBottomSheet = CustomSleepTimerBottomSheet(songService)
                        customSleepTimerBottomSheet.show(supportFragmentManager, customSleepTimerBottomSheet.tag)
                        dismiss()
                    }
                    SleepTimerModel.TURN_OFF -> {
                        songService.setSleepTimerModel(null, 0)
                        dismiss()
                    }
                    else -> {
                        songService.setSleepTimerModel(model, model.time)
                        dismiss()
                    }
                }
            }

            if (model == SleepTimerModel.TURN_OFF) {
                if (songService.isSleepTimerEnabled) {
                    binding.llGroup.addView(itemBinding.root)
                }
            } else {
                binding.llGroup.addView(itemBinding.root)
            }
        }

        binding.tvSleepTimer.text = if (songService.isSleepTimerEnabled) {
            if (songService.sleepTimerModel.value == SleepTimerModel.END_OF_TRACK) {
                "${getString(R.string.sleep_timer)} - ${getString(R.string.end_of_track)}"
            } else {
                "${getString(R.string.sleep_timer)} - ${Formatter.formatTime(songService.sleepTimerRemainingTime.value ?: 0)} ${getString(R.string.left)}"
            }
        } else {
            getString(R.string.sleep_timer)
        }
    }

    private fun setupListeners() {
        binding.rlCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setupObservers() {
        songService.sleepTimerRemainingTime.observe(this) {
            if (songService.isSleepTimerEnabled && songService.sleepTimerModel.value != SleepTimerModel.END_OF_TRACK) {
                binding.tvSleepTimer.text = "${getString(R.string.sleep_timer)} - ${Formatter.formatTime(songService.sleepTimerRemainingTime.value ?: 0)} ${getString(R.string.left)}"
            }
        }
    }
}