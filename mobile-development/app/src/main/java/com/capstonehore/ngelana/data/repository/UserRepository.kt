package com.capstonehore.ngelana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.UserInformationItem
import com.capstonehore.ngelana.data.remote.response.users.LoginResponse
import com.capstonehore.ngelana.data.remote.response.users.RegisterResponse
import com.capstonehore.ngelana.data.remote.response.users.UserResponse
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
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

    private suspend fun getToken(): String? = token ?: runBlocking {
        userPreferences.getToken().first()
    }.also { token = it }

    private suspend fun getUserId(): String? =
        userId ?: userPreferences.getUserId().first().also { userId = it }

//    fun register(
//        name: String,
//        email: String,
//        password: String
//    ): LiveData<Result<RegisterResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.register(name, email, password)
//
//            emit(Result.Success(response))
////        } catch (e: HttpException) {
////            val errorBody = e.response()?.errorBody()?.string()
////            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
////
////            emit(Result.Error(errorResponse.message.toString()))
////        }
//        } catch (e: Exception) {
//            Log.d(TAG, "register: ${e.message}")
//
//            emit(Result.Error(e.message.toString()))
//        }
//    }
//
//    fun login(
//        email: String,
//        password: String,
//    ): LiveData<Result<LoginResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.login(email, password)
//
//            emit(Result.Success(response))
//        } catch (e: HttpException) {
//            val errorBody = e.response()?.errorBody()?.string()
//            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
//
//            emit(Result.Error(errorResponse.message.toString()))
//        } catch (e: Exception) {
//            Log.d(TAG, "login  : ${e.message}")
//
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    fun getUserById(): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.getUserById(userId)
                emit(Result.Success(response))
            } else {
                emit(Result.Error("User ID not found"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UserResponse::class.java)

            emit(Result.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getUserById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateUserById(userInformationItem: UserInformationItem): LiveData<Result<UserResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val token = getToken()
                val userId = getUserId()

                apiService = ApiConfig.getApiService(token.toString())
                if (userId != null) {
                    val response = apiService.updateUserById(userId, userInformationItem)
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error("User ID not found"))
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, UserResponse::class.java)

                emit(Result.Error(errorResponse.message.toString()))
            } catch (e: Exception) {
                Log.d(TAG, "updateUserById: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun deleteUserById(): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.deleteUserById(userId)
                emit(Result.Success(response))
            } else {
                emit(Result.Error("User ID not found"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UserResponse::class.java)

            emit(Result.Error(errorResponse.message.toString()))
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