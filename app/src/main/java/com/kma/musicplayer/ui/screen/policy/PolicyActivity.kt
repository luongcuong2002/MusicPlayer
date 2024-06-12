package com.kma.musicplayer.ui.screen.policy

import android.os.Bundle
import android.webkit.WebSettings
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.ActivityPolicyBinding
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.ui.screen.core.BaseActivity

class PolicyActivity : BaseActivity<ActivityPolicyBinding>() {

    companion object {
        const val PRIVACY_POLICY_LINK = "https://firebasestorage.googleapis.com/v0/b/asa134-cast-to-tv.appspot.com/o/Privacy-Policy.html?alt=media&token=2160578b-06d3-4837-8cb7-ece49923b019"
    }

    override fun getContentView(): Int = R.layout.activity_policy

    override fun onThemeChanged(theme: Theme) {
        binding.root.setBackgroundColor(getColor(theme.backgroundColorRes))
        binding.backButton.setImageResource(theme.imageBackButtonRes)
        binding.tvTitle.setTextColor(getColor(theme.titleTextColorRes))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webSettings: WebSettings = binding.webview.settings
        webSettings.javaScriptEnabled = true
        binding.webview.loadUrl(PRIVACY_POLICY_LINK)
    }
}