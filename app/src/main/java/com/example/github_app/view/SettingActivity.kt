package com.example.github_app.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.github_app.R
import com.example.github_app.preference.Setting
import com.example.github_app.viewmodel.MainViewModel


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")
class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val switchTheme = findViewById<SwitchCompat>(R.id.switch_compat)

        val preference = Setting.getInstance(dataStore)
        val settingViewModelFactory = SettingViewModel(preference).createFactory()
        val settingViewModel = ViewModelProvider(this, settingViewModelFactory)[SettingViewModel::class.java]
        settingViewModel.themeSetting().observe(this) {isDarkMode ->
            if (isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
            switchTheme.setOnCheckedChangeListener { compoundButton, isDarkMode ->
                settingViewModel.saveSetting(isDarkMode)
            }
        }
    }
}