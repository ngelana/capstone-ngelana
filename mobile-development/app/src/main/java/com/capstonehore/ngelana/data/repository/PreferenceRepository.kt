package com.capstonehore.ngelana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.data.remote.response.preferences.PreferencesResponse
import com.capstonehore.ngelana.data.remote.response.preferences.PreferencesResponseByUserId
import com.capstonehore.ngelana.data.remote.response.preferences.UserDataPreferencesItem
import com.capstonehore.ngelana.data.remote.response.preferences.UserPreferenceResponse
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class PreferenceRepository (
    private var apiService: ApiService,
    private val userPreferences: UserPreferences,
) {

    private var token: String? = null
    private var userId: String? = null

    private suspend fun getToken(): String? = token ?: runBlocking {
        userPreferences.getToken().first()
    }.also { token = it }

    private suspend fun getUserId(): String? =
        userId ?: userPreferences.getUserId().first().also { userId = it }

    fun getAllPreferences(): LiveData<Result<List<PreferenceItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            apiService = ApiConfig.getApiService(token.toString())

            val response = apiService.getAllPreferences()
            val preferenceItem = response.data ?: emptyList()

            emit(Result.Success(preferenceItem))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PreferencesResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getUserPreferencesById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun createUserPreference(userDataPreferencesItems: List<UserDataPreferencesItem>):
            LiveData<Result<List<UserDataPreferencesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val apiService = ApiConfig.getApiService(token.toString())

            val response = apiService.createUserPreference(userDataPreferencesItems)
            val userDataPreferences = response.data ?: emptyList()

            emit(Result.Success(userDataPreferences))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UserPreferenceResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "createUserPreference: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPreferenceByUserId(): LiveData<Result<List<PreferenceItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            val apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.getPreferenceByUserId(userId)
                val userPreference = response.user
                val userPreferenceItem = userPreference?.userPreferences ?: emptyList()

                emit(Result.Success(userPreferenceItem.map { it.preference ?: PreferenceItem() }))
            } else {
                emit(Result.Error("User ID not found"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PreferencesResponseByUserId::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getPreferenceById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateUserPreference(preferenceItem: List<PreferenceItem>): LiveData<Result<List<PreferenceItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            val apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.updateUserPreference(userId, preferenceItem)
                val updatedPreferences = response.data ?: emptyList()

                emit(Result.Success(updatedPreferences.map { it.preference ?: PreferenceItem() }))
            } else {
                emit(Result.Error("User ID not found"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UserPreferenceResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "updateUserPreference: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

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