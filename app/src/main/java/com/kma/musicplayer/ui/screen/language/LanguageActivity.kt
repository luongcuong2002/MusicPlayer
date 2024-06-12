package com.kma.musicplayer.ui.screen.language

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.kma.musicplayer.R
import com.kma.musicplayer.databinding.ActivityLanguageBinding
import com.kma.musicplayer.model.LanguageModel
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.ui.screen.core.BaseActivity
import com.kma.musicplayer.ui.screen.main.MainActivity
import com.kma.musicplayer.utils.SystemUtil

class LanguageActivity : BaseActivity<ActivityLanguageBinding>(),
    IClickLanguage {

    private var adapter: LanguageAdapter? = null
    private var model: LanguageModel? = null
    private var sharedPreferences: SharedPreferences? = null

    private lateinit var languageViewModel: LanguageViewModel

    override fun getContentView(): Int = R.layout.activity_language

    override fun onThemeChanged(theme: Theme) {
        binding.root.setBackgroundColor(resources.getColor(theme.backgroundColorRes))
        binding.backButton.setImageResource(theme.imageBackButtonRes)
        binding.tvTitle.setTextColor(resources.getColor(theme.titleTextColorRes))
        adapter?.setTheme(theme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languageViewModel = ViewModelProvider(this).get(LanguageViewModel::class.java)
        sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE)
        adapter =
            LanguageAdapter(
                this,
                setLanguageDefault(),
                this
            )
        binding.rclLanguage.adapter = adapter

        binding.ivDone.setOnClickListener {
            ivDone()
        }
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
            SystemUtil.setPreLanguage(this@LanguageActivity, model?.isoLanguage)
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