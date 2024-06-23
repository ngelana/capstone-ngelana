package com.capstonehore.ngelana.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.capstonehore.ngelana.data.local.database.NgelanaRoomDatabase
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.repository.FavoriteRepository
import com.capstonehore.ngelana.data.repository.PlaceRepository
import com.capstonehore.ngelana.data.repository.PlanRepository
import com.capstonehore.ngelana.data.repository.PreferenceRepository
import com.capstonehore.ngelana.data.repository.Repository
import com.capstonehore.ngelana.data.repository.ReviewRepository
import com.capstonehore.ngelana.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

object Injection {

    fun provideRepository(context: Context): Repository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val token = runBlocking { pref.getToken().first() }
        val apiService = ApiConfig.getApiService(token.toString())
        val ngelanaRoomDatabase = NgelanaRoomDatabase.getDatabase(context)

        return Repository.getInstance(apiService, pref, ngelanaRoomDatabase)
    }

//    fun provideUserRepository(context: Context): UserRepository {
//        val pref = UserPreferences.getInstance(context.dataStore)
//        val token = runBlocking { pref.getToken().first() }
//        val apiService = ApiConfig.getApiService(token.toString())
//
//        return UserRepository.getInstance(apiService, pref)
//    }

//    fun providePlaceRepository(context: Context): PlaceRepository {
//        val pref = UserPreferences.getInstance(context.dataStore)
//        val token = runBlocking { pref.getToken().first() }
//        val apiService = ApiConfig.getApiService(token.toString())
//
//        return PlaceRepository.getInstance(apiService, pref)
//    }

//    fun providePlanRepository(context: Context): PlanRepository {
//        val pref = UserPreferences.getInstance(context.dataStore)
//        val token = runBlocking { pref.getToken().first() }
//        val apiService = ApiConfig.getApiService(token.toString())
//
//        return PlanRepository.getInstance(apiService, pref)
//    }

//    fun providePreferenceRepository(context: Context): PreferenceRepository {
//        val pref = UserPreferences.getInstance(context.dataStore)
//        val token = runBlocking { pref.getToken().first() }
//        val apiService = ApiConfig.getApiService(token.toString())
//
//        return PreferenceRepository.getInstance(apiService, pref)
//    }

//    fun provideReviewRepository(context: Context): ReviewRepository {
//        val pref = UserPreferences.getInstance(context.dataStore)
//        val token = runBlocking { pref.getToken().first() }
//        val apiService = ApiConfig.getApiService(token.toString())
//
//        return ReviewRepository.getInstance(apiService, pref)
//    }

//    fun provideFavoriteRepository(context: Context): FavoriteRepository {
//        val ngelanaRoomDatabase = NgelanaRoomDatabase.getDatabase(context)
//
//        return FavoriteRepository.getInstance(ngelanaRoomDatabase)
//    }
}