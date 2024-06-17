package com.capstonehore.ngelana.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    fun isLoggedIn(): Flow<Boolean?> = dataStore.data.map {
        it[STATE_KEY]
    }

    fun getToken(): Flow<String?> = dataStore.data.map {
        it[TOKEN_KEY]
    }

    suspend fun saveToken(token: String) = dataStore.edit {
        it[TOKEN_KEY] = token
    }

    suspend fun logout() = dataStore.edit {
        it[TOKEN_KEY] = ""
        it[STATE_KEY] = false
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}