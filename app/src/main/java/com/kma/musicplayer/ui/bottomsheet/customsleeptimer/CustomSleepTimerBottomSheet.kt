package com.kma.musicplayer.ui.bottomsheet.customsleeptimer

import android.annotation.SuppressLint
import android.app.Dialog
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.BottomSheetCustomSleepTimerBinding
import com.kma.musicplayer.model.SleepTimerModel
import com.kma.musicplayer.service.PlaySongService
import com.kma.musicplayer.ui.customview.WheelView

class CustomSleepTimerBottomSheet(
    private val songService: PlaySongService
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetCustomSleepTimerBinding

    private var hours = 0
    private var minutes = 0

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = BottomSheetCustomSleepTimerBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        initView()
        setupListeners()
    }

    private fun initView() {
        binding.hourWheelView.setDataItems((0 until 25).map { "%02d".format(it) }.toMutableList())
        binding.hourWheelView.setSelectedItemPosition(0)
        binding.minuteWheelView.setDataItems((0 until 60).map { "%02d".format(it) }.toMutableList())
        binding.minuteWheelView.setSelectedItemPosition(0)
        binding.hourWheelView.setOnItemSelectedListener(object : WheelView.OnItemSelectedListener {
            override fun onItemSelected(wheelView: WheelView, data: Any, position: Int) {
                hours = (data as String).toInt()
            }
        })
        binding.minuteWheelView.setOnItemSelectedListener(object : WheelView.OnItemSelectedListener {
            override fun onItemSelected(wheelView: WheelView, data: Any, position: Int) {
                minutes = (data as String).toInt()
            }
        })
    }

    private fun setupListeners() {
        binding.rlCancel.setOnClickListener {
            dismiss()
        }
        binding.rlSave.setOnClickListener {
            if (hours == 0 && minutes == 0) {
                Toast.makeText(requireContext(), requireContext().getString(R.string.please_select_a_time), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            songService.setSleepTimerModel(SleepTimerModel.CUSTOM, (hours * 60 + minutes) * 60L)
            dismiss()
        }
    }
}