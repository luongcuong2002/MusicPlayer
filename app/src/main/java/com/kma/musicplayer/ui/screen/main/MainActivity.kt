package com.kma.musicplayer.ui.screen.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kma.musicplayer.databinding.ActivityMainBinding
import com.kma.musicplayer.ui.screen.core.BaseActivity
import com.kma.musicplayer.R

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var navController: NavController

    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController(R.id.nav_host_fragment)
        setupListeners()
    }

    private fun setupListeners() {
        mDataBinding.llTabHome.setOnClickListener {
            if (navController.currentDestination?.id != R.id.homeFragment) {
                selectTab(R.id.homeFragment)
                navController.navigate(R.id.homeFragment)
            }
        }
        mDataBinding.llTabFavourite.setOnClickListener {
            if (navController.currentDestination?.id != R.id.favouriteFragment) {
                selectTab(R.id.favouriteFragment)
                navController.navigate(R.id.favouriteFragment)
            }
        }
        mDataBinding.llTabLocal.setOnClickListener {
            if (navController.currentDestination?.id != R.id.musicLocalFragment) {
                selectTab(R.id.musicLocalFragment)
                navController.navigate(R.id.musicLocalFragment)
            }
        }
        mDataBinding.llTabSetting.setOnClickListener {
            if (navController.currentDestination?.id != R.id.settingFragment) {
                selectTab(R.id.settingFragment)
                navController.navigate(R.id.settingFragment)
            }
        }
    }

    private fun selectTab(tabId: Int) {
        unSelectAllTabs()
        when (tabId) {
            R.id.homeFragment -> {
                mDataBinding.ivHome.setImageResource(R.drawable.ic_tab_home_active)
                mDataBinding.tvHome.setTextColor(resources.getColor(R.color.color_7150D0))
            }
            R.id.favouriteFragment -> {
                mDataBinding.ivFavourite.setImageResource(R.drawable.ic_tab_favourite_active)
                mDataBinding.tvFavourite.setTextColor(resources.getColor(R.color.color_7150D0))
            }
            R.id.musicLocalFragment -> {
                mDataBinding.ivLocal.setImageResource(R.drawable.ic_tab_local_active)
                mDataBinding.tvLocal.setTextColor(resources.getColor(R.color.color_7150D0))
            }
            R.id.settingFragment -> {
                mDataBinding.ivSetting.setImageResource(R.drawable.ic_tab_setting_active)
                mDataBinding.tvSetting.setTextColor(resources.getColor(R.color.color_7150D0))
            }
        }
    }

    private fun unSelectAllTabs() {
        mDataBinding.ivHome.setImageResource(R.drawable.ic_tab_home_inactive)
        mDataBinding.tvHome.setTextColor(resources.getColor(R.color.color_787B82))
        mDataBinding.ivFavourite.setImageResource(R.drawable.ic_tab_favourite_inactive)
        mDataBinding.tvFavourite.setTextColor(resources.getColor(R.color.color_787B82))
        mDataBinding.ivLocal.setImageResource(R.drawable.ic_tab_local_inactive)
        mDataBinding.tvLocal.setTextColor(resources.getColor(R.color.color_787B82))
        mDataBinding.ivSetting.setImageResource(R.drawable.ic_tab_setting_inactive)
        mDataBinding.tvSetting.setTextColor(resources.getColor(R.color.color_787B82))
    }
}