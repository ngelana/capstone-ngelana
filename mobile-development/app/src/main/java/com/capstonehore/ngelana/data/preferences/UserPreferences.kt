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

    //! Check if user is logged in
    fun isLoggedIn(): Flow<Boolean?> = dataStore.data.map { it[STATE_KEY] }

    //! Get token from data store
    fun getToken(): Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }

    //! Save token to data store
    suspend fun saveToken(token: String) = dataStore.edit { it[TOKEN_KEY] = token }

    suspend fun saveUserEmail(email: String) {
        dataStore.edit { preferences ->
//            preferences[EMAIL_KEY] = email
        }
    }

//    fun getUserEmail(): Flow<String?> {
//        return dataStore.data.map { preferences ->
//            preferences[EMAIL_KEY]
//        }
//    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = ""
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        private var INSTANCE: UserPreferences? = null
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

    }
}