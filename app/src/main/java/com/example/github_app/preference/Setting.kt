package com.example.github_app.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Setting private constructor(private val dataStore: DataStore<Preferences>) {
    private val KEY_THEME = booleanPreferencesKey("switch_theme")

    fun themeSetting(): Flow<Boolean> {
        return dataStore.data.map { pref -> pref[KEY_THEME] ?: false }
    }

    suspend fun saveSetting (darkModeActivated: Boolean){
        dataStore.edit { pref -> pref[KEY_THEME] = darkModeActivated }
    }

    companion object{
        @Volatile
        private var INSTANCE: Setting?= null

        fun getInstance(dataStore: DataStore<Preferences>): Setting{
            return INSTANCE ?: synchronized(this){
                val instance = Setting(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}