package com.capstonehore.ngelana.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    suspend fun prefLogin() {
        dataStore.edit {
            it[STATE_KEY] = true
        }
    }

    fun isLoggedIn(): Flow<Boolean?> = dataStore.data.map {
        it[STATE_KEY]
    }

    fun getToken(): Flow<String?> = dataStore.data.map {
        it[TOKEN_KEY]
    }

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    fun getUserId(): Flow<String?> = dataStore.data.map {
        it[USER_ID_KEY]
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit {
            it[USER_ID_KEY] = userId
        }
    }

    fun getUserPreferenceId(): Flow<String?> = dataStore.data.map {
        it[USER_PREFERENCE_ID_KEY]
    }

    suspend fun hasUserPreferences(): Boolean {
        val userPreferenceId = getUserPreferenceId().first()
        return !userPreferenceId.isNullOrEmpty()
    }

    suspend fun logout() = dataStore.edit {
        it[TOKEN_KEY] = ""
        it[USER_ID_KEY] = ""
        it[USER_PREFERENCE_ID_KEY] = ""
        it[STATE_KEY] = false
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_PREFERENCE_ID_KEY = stringPreferencesKey("user_preference_id")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}