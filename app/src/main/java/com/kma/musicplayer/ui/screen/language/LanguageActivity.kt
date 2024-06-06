package com.casttv.screenmirror.castvideo.castweb.ui.screen.language_setting

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.util.Log
import android.widget.Toast
import  com.casttv.screenmirror.castvideo.castweb.R
import com.casttv.screenmirror.castvideo.castweb.databinding.ActivityLanguageSettingBinding
import com.casttv.screenmirror.castvideo.castweb.ui.core.BaseActivity
import com.casttv.screenmirror.castvideo.castweb.ui.model.LanguageModel
import com.casttv.screenmirror.castvideo.castweb.ui.screen.language_first_open.IClickLanguage
import com.casttv.screenmirror.castvideo.castweb.ui.screen.language_first_open.LanguageAdapter
import com.casttv.screenmirror.castvideo.castweb.ui.screen.main.MainActivity
import com.casttv.screenmirror.castvideo.castweb.utils.SystemUtil

class LanguageSettingActivity : BaseActivity<LanguageSettingViewModel, ActivityLanguageSettingBinding>(),
    IClickLanguage {

    private var adapter: LanguageAdapter? = null
    private var model: LanguageModel? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun createViewModel(): Class<LanguageSettingViewModel> = LanguageSettingViewModel::class.java

    override fun getContentView(): Int = R.layout.activity_language_setting

    override fun initView() {
        sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE)
        adapter =
            LanguageAdapter(
                this,
                setLanguageDefault(),
                this
            )
        mDataBinding.rclLanguage.adapter = adapter

        mDataBinding.ivDone.setOnClickListener {
            ivDone()
        }
    }

    override fun bindViewModel() {

    }

    override fun onBackPressedCallback() {
        finishAffinity()
    }

    override fun onClick(data: LanguageModel) {
        adapter?.setSelectLanguage(data)
        model = data
    }

    private fun setLanguageDefault(): List<LanguageModel> {
        val lists: MutableList<LanguageModel> = ArrayList()
        lists.add(LanguageModel("English", "en", false, R.drawable.ic_english_flag))
        lists.add(LanguageModel("Hindi", "hi", false, R.drawable.ic_hindi_flag))
        lists.add(LanguageModel("Spanish", "es", false, R.drawable.ic_span_flag))
        lists.add(LanguageModel("French", "fr", false, R.drawable.ic_french_flag))
        lists.add(LanguageModel("German", "de", false, R.drawable.ic_german_flag))
        lists.add(LanguageModel("Indonesian", "in", false, R.drawable.ic_indo_flag))
        lists.add(LanguageModel("Portuguese", "pt", false, R.drawable.ic_portuguese_flag))

        val key: String = SystemUtil.getPreLanguage(this)
        var lang = "en"
        lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0].language
        } else {
            Resources.getSystem().configuration.locale.language
        }
        var count = 0
        for (model: LanguageModel in lists) {
            if (model.isoLanguage == lang) {
                count++
            }
        }
        if (count == 0) {
            model = LanguageModel("English", "en", false, R.drawable.ic_english_flag)
            adapter?.setSelectLanguage(model)
        }
        for (model: LanguageModel in lists) {
            if (model.isoLanguage == SystemUtil.getPreLanguage(this)) {
                this.model = model
            }
        }
        Log.e("", "setLanguageDefault: $key")
        for (i in lists.indices) {
            if (!sharedPreferences!!.getBoolean("nativeLanguage", false)) {
                if (key == lists[i].isoLanguage) {
                    val data = lists[i]
                    data.isCheck = true
                    lists.remove(lists[i])
                    lists.add(0, data)
                    break
                }
            } else {
                if (key == lists[i].isoLanguage) {
                    lists[i].isCheck = true
                }
            }
        }

        return lists
    }

    fun ivDone() {
        if (model != null) {
            SystemUtil.setPreLanguage(this@LanguageSettingActivity, model?.isoLanguage)
            SystemUtil.setLocale(this)
            startNextAct()
        }else{
            Toast.makeText(this, resources.getString(R.string.you_need_to_select_language), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startNextAct() {
        showActivity(MainActivity::class.java, null)
        finish()
    }
}