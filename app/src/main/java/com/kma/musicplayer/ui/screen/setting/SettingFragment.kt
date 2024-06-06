package com.kma.musicplayer.ui.screen.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kma.musicplayer.ui.screen.language.LanguageActivity
import com.kma.musicplayer.BuildConfig
import com.kma.musicplayer.databinding.FragmentSettingBinding
import com.kma.musicplayer.ui.screen.core.BaseFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.ui.dialog.RatingDialog
import com.kma.musicplayer.ui.screen.addwidget.AddWidgetActivity
import com.kma.musicplayer.ui.screen.hiddensong.HiddenSongActivity
import com.kma.musicplayer.ui.screen.policy.PolicyActivity

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun getContentView(): Int = R.layout.fragment_setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        binding.llHiddenSongs.setOnClickListener {
            showActivity(HiddenSongActivity::class.java)
        }
        binding.llAddWidget.setOnClickListener {
            showActivity(AddWidgetActivity::class.java)
        }
        binding.llRingtone.setOnClickListener {

        }
        binding.llLanguage.setOnClickListener {
            showActivity(LanguageActivity::class.java)
        }
        binding.llShare.setOnClickListener {
            shareApp()
        }
        binding.llRate.setOnClickListener {
            showRatingDialog()
        }
        binding.llPrivacyPolicy.setOnClickListener {
            showActivity(PolicyActivity::class.java)
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        var shareMessage = getString(R.string.app_name)
        shareMessage =
            "$shareMessage \nhttps://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "Share to"))
    }

    private fun showRatingDialog() {
        RatingDialog(requireActivity()).show()
    }
}