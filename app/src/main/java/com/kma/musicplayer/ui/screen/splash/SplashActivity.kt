package com.kma.musicplayer.ui.screen.splash

import android.os.Bundle
import android.os.Handler
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.ActivitySplashBinding
import com.kma.musicplayer.ui.screen.core.BaseActivity
import com.kma.musicplayer.ui.screen.main.MainActivity


class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun getContentView(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            // Start your app main activity
            showActivity(MainActivity::class.java)
        }, 2000)
    }
}