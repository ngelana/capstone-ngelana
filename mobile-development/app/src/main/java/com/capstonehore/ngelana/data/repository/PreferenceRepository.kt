package com.capstonehore.ngelana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.data.remote.response.preferences.PreferenceModel
import com.capstonehore.ngelana.data.remote.response.preferences.UserDataPreferencesItem
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first

class PreferenceRepository (
    private var apiService: ApiService,
    private val userPreferences: UserPreferences,
) {

    private var token: String? = null
    private var userId: String? = null

    private suspend fun getToken(): String {
        if (token.isNullOrEmpty()) {
            token = userPreferences.getToken().first()
        }
        return token ?: ""
    }

    private suspend fun getUserId(): String {
        if (userId.isNullOrEmpty()) {
            userId = userPreferences.getUserId().first()
        }
        return userId ?: ""
    }

    private suspend fun initializeApiService() {
        val token = getToken()
        apiService = ApiConfig.getApiService(token)
    }

    fun getAllPreferences(): LiveData<Result<List<PreferenceItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val response = apiService.getAllPreferences()
            val preferenceItem = response.data ?: emptyList()

            emit(Result.Success(preferenceItem))
        } catch (e: Exception) {
            Log.d(TAG, "getUserPreferencesById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun createUserPreference(preferenceIds: List<String>): LiveData<Result<List<UserDataPreferencesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val userId = getUserId()

            val request = PreferenceModel(preferenceIds, userId)
            val response = apiService.createUserPreference(request)

            when {
                response.data != null -> {
                    val userDataPreferences = response.data
                    Log.d(TAG, "createUserPreference: ${response.message.toString()}")

                    emit(Result.Success(userDataPreferences))
                }
                response.message != null -> {
                    val errorMessage = response.message.toString()
                    emit(Result.Error(errorMessage))
                }
                else -> {
                    emit(Result.Error("Unknown error"))
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "createUserPreference: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPreferenceByUserId(): LiveData<Result<List<PreferenceItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val userId = getUserId()
            val response = apiService.getPreferenceByUserId(userId)

            when {
                response.data != null -> {
                    val userPreferences = response.data.userPreferences
                    val preferenceItem = userPreferences?.map { item -> item.preference ?: PreferenceItem() } ?: emptyList()
                    Log.d(TAG, "getPreferenceByUserId: ${response.message.toString()}")

                    emit(Result.Success(preferenceItem))
                }
                response.message != null -> {
                    val errorMessage = response.message.toString()
                    emit(Result.Error(errorMessage))
                }
                else -> {
                    emit(Result.Error("Unknown error"))
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "getPreferenceByUserId: ${e.message}")
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

//    fun updateUserPreference(preferenceItem: List<PreferenceItem>): LiveData<Result<List<PreferenceItem>>> = liveData {
//        emit(Result.Loading)
//        try {
//            initializeApiService()
//
//            val userId = getUserId()
//            val response = apiService.updateUserPreference(userId, preferenceItem)
//            val updatedPreferences = response.data ?: emptyList()
//
//            emit(Result.Success(updatedPreferences.map { it.preference ?: PreferenceItem() }))
//        } catch (e: Exception) {
//            Log.d(TAG, "updateUserPreference: ${e.message}")
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    companion object {
        private const val TAG = "PreferenceRepository"

        private var instance: PreferenceRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences,
        ): PreferenceRepository {
            return instance ?: synchronized(this) {
                instance ?: PreferenceRepository(
                    apiService,
                    userPreference,
                ).also {
                    instance = it
                }
            }
        }
    }
}