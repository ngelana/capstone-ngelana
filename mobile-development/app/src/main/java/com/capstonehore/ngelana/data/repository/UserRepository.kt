package com.capstonehore.ngelana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.UserInformationItem
import com.capstonehore.ngelana.data.remote.response.users.LoginModel
import com.capstonehore.ngelana.data.remote.response.users.LoginResponse
import com.capstonehore.ngelana.data.remote.response.users.RegisterModel
import com.capstonehore.ngelana.data.remote.response.users.RegisterResponse
import com.capstonehore.ngelana.data.remote.response.users.UserResponse
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import com.capstonehore.ngelana.data.repository.Repository.Companion
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class UserRepository (
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

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = RegisterModel(name, email, password)
            val response = apiService.register(request)

            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "register: ${e.message}")

            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(
        email: String,
        password: String,
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = LoginModel(email, password)
            val response = apiService.login(request)

            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "login  : ${e.message}")

            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserById(): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val userId = getUserId()
            val response = apiService.getUserById(userId)

            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "getUserById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateUserById(userInformationItem: UserInformationItem): LiveData<Result<UserResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                initializeApiService()

                val userId = getUserId()
                val response = apiService.updateUserById(userId, userInformationItem)

                emit(Result.Success(response))
            } catch (e: Exception) {
                Log.d(TAG, "updateUserById: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun deleteUserById(): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val userId = getUserId()
            val response = apiService.deleteUserById(userId)

            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "deleteUserById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        private const val TAG = "UserRepository"

        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences,
        ): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(
                    apiService,
                    userPreference,
                ).also {
                    instance = it
                }
            }
        }
    }
}