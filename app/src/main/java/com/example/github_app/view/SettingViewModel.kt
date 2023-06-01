package com.example.github_app.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.github_app.preference.Setting
import kotlinx.coroutines.launch

class SettingViewModel(private var preference: Setting): ViewModel() {
    fun themeSetting(): LiveData<Boolean> {
        return preference.themeSetting().asLiveData()
    }

    fun saveSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            preference.saveSetting(isDarkMode)
        }
    }
}