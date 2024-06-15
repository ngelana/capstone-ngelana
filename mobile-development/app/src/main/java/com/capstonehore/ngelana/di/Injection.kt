package com.capstonehore.ngelana.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.capstonehore.ngelana.data.local.database.NgelanaRoomDatabase
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.repository.GeneralRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

object Injection {
    fun provideRepository(context: Context): GeneralRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val token = runBlocking { pref.getToken().first() }
        val apiService = ApiConfig.getApiService(token.toString())

        val ngelanaRoomDatabase = NgelanaRoomDatabase.getDatabase(context)
        return GeneralRepository.getInstance(apiService, pref, ngelanaRoomDatabase)
    }
}