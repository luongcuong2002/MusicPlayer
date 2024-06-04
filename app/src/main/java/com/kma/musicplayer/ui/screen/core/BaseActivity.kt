package com.kma.musicplayer.ui.screen.core

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.kma.musicplayer.service.PlaySongService
import com.kma.musicplayer.service.ServiceController
import com.kma.musicplayer.utils.SystemUtil

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity(), ServiceConnection {

    lateinit var binding: DB
    private var currentApiVersion = 0

    private var _songService: PlaySongService? = null
    val songService: PlaySongService?
        get() = _songService

    private var mBound = false
    override fun onServiceConnected(className: ComponentName, service: IBinder) {
        val binder = service as PlaySongService.LocalBinder
        _songService = binder.getService()
        mBound = true
    }

    override fun onServiceDisconnected(className: ComponentName) {
        mBound = false
    }

    fun isServiceBound(): Boolean {
        return mBound
    }

    abstract fun getContentView(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtil.setLocale(this)
        currentApiVersion = Build.VERSION.SDK_INT
        val flags: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = flags
            val decorView: View = window.decorView
            decorView
                .setOnSystemUiVisibilityChangeListener { visibility ->
                    if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        decorView.systemUiVisibility = flags
                    }
                }
        }
        binding = DataBindingUtil.setContentView(this, getContentView())

        Log.d("CHECK_ACTIVITY", "onCreate: ${javaClass.simpleName}")
    }

    override fun onStart() {
        super.onStart()
        if (ServiceController.shouldBindService) {
            bindService(
                Intent(this, PlaySongService::class.java),
                this,
                BIND_AUTO_CREATE
            )
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(this)
            mBound = false
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    fun showActivity(clazz: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }
}