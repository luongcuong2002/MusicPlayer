package com.kma.musicplayer.ui.screen.setting

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.kma.musicplayer.ui.screen.language.LanguageActivity
import com.kma.musicplayer.BuildConfig
import com.kma.musicplayer.databinding.FragmentSettingBinding
import com.kma.musicplayer.ui.screen.core.BaseFragment
import com.kma.musicplayer.R
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.ui.dialog.RatingDialog
import com.kma.musicplayer.ui.screen.addwidget.AddWidgetActivity
import com.kma.musicplayer.ui.screen.hiddensong.HiddenSongActivity
import com.kma.musicplayer.ui.screen.policy.PolicyActivity

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun getContentView(): Int = R.layout.fragment_setting

    override fun onThemeChanged(theme: Theme) {
        binding.tvTitle.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.tvGeneral.setTextColor(requireActivity().getColor(theme.adapterTitleTextColorRes))
        binding.tvHelp.setTextColor(requireActivity().getColor(theme.adapterTitleTextColorRes))
        binding.ivLightMode.setColorFilter(requireActivity().getColor(theme.settingIconColorRes))
        binding.tvLightMode.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.ivHiddenSongs.setColorFilter(requireActivity().getColor(theme.settingIconColorRes))
        binding.tvHiddenSongs.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.ivAddWidget.setColorFilter(requireActivity().getColor(theme.settingIconColorRes))
        binding.tvAddWidget.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.ivRingtone.setColorFilter(requireActivity().getColor(theme.settingIconColorRes))
        binding.tvRingtone.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.ivLanguage.setColorFilter(requireActivity().getColor(theme.settingIconColorRes))
        binding.tvLanguage.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.ivShare.setColorFilter(requireActivity().getColor(theme.settingIconColorRes))
        binding.tvShare.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.ivRate.setColorFilter(requireActivity().getColor(theme.settingIconColorRes))
        binding.tvRate.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.ivPrivacyPolicy.setColorFilter(requireActivity().getColor(theme.settingIconColorRes))
        binding.tvPrivacyPolicy.setTextColor(requireActivity().getColor(theme.titleTextColorRes))
        binding.llGeneral.setBackgroundResource(theme.backgroundSettingTabRes)
        binding.llHelp.setBackgroundResource(theme.backgroundSettingTabRes)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupListener()

        getThemeViewModel().theme.observe(viewLifecycleOwner) {
            onThemeChanged(it)
        }
    }

    private fun initView() {
        binding.switchLightMode.post {
            binding.switchLightMode.setChecked(getThemeViewModel().theme.value == Theme.LIGHT)
        }
    }

    private fun setupListener() {
        binding.switchLightMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getThemeViewModel().setCurrentTheme(Theme.LIGHT)
            } else {
                getThemeViewModel().setCurrentTheme(Theme.DARK)
            }
        }
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