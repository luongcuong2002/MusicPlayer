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
        binding.llTabHome.setOnClickListener {
            if (navController.currentDestination?.id != R.id.homeFragment) {
                selectTab(R.id.homeFragment)
                navController.navigate(R.id.homeFragment)
            }
        }
        binding.llTabFavourite.setOnClickListener {
            if (navController.currentDestination?.id != R.id.favouriteFragment) {
                selectTab(R.id.favouriteFragment)
                navController.navigate(R.id.favouriteFragment)
            }
        }
        binding.llTabLocal.setOnClickListener {
            if (navController.currentDestination?.id != R.id.musicLocalFragment) {
                selectTab(R.id.musicLocalFragment)
                navController.navigate(R.id.musicLocalFragment)
            }
        }
        binding.llTabSetting.setOnClickListener {
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
                binding.ivHome.setImageResource(R.drawable.ic_tab_home_active)
                binding.tvHome.setTextColor(resources.getColor(R.color.color_7150D0))
            }
            R.id.favouriteFragment -> {
                binding.ivFavourite.setImageResource(R.drawable.ic_tab_favourite_active)
                binding.tvFavourite.setTextColor(resources.getColor(R.color.color_7150D0))
            }
            R.id.musicLocalFragment -> {
                binding.ivLocal.setImageResource(R.drawable.ic_tab_local_active)
                binding.tvLocal.setTextColor(resources.getColor(R.color.color_7150D0))
            }
            R.id.settingFragment -> {
                binding.ivSetting.setImageResource(R.drawable.ic_tab_setting_active)
                binding.tvSetting.setTextColor(resources.getColor(R.color.color_7150D0))
            }
        }
    }

    private fun unSelectAllTabs() {
        binding.ivHome.setImageResource(R.drawable.ic_tab_home_inactive)
        binding.tvHome.setTextColor(resources.getColor(R.color.color_787B82))
        binding.ivFavourite.setImageResource(R.drawable.ic_tab_favourite_inactive)
        binding.tvFavourite.setTextColor(resources.getColor(R.color.color_787B82))
        binding.ivLocal.setImageResource(R.drawable.ic_tab_local_inactive)
        binding.tvLocal.setTextColor(resources.getColor(R.color.color_787B82))
        binding.ivSetting.setImageResource(R.drawable.ic_tab_setting_inactive)
        binding.tvSetting.setTextColor(resources.getColor(R.color.color_787B82))
    }
}