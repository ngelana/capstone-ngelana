package com.capstonehore.ngelana.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeManager private constructor(private val dataStore: DataStore<Preferences>) {

    private val key = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[key] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[key] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemeManager? = null

        fun getInstance(dataStore: DataStore<Preferences>): ThemeManager {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemeManager(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}