package com.capstonehore.ngelana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.DataPlacesItem
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first

class PlanRepository (
    private var apiService: ApiService,
    private val userPreferences: UserPreferences,
) {

    private var token: String? = null
    private var userId: String? = null
    private var userPreferenceId: String? = null

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

//    private suspend fun getUserPreferenceId(): String {
//        if (userPreferenceId.isNullOrEmpty()) {
//            userPreferenceId = userPreferences.getUserPreferenceId().first()
//        }
//        return userPreferenceId ?: ""
//    }

    private suspend fun initializeApiService() {
        val token = getToken()
        apiService = ApiConfig.getApiService(token)
    }

//    fun getRecommendedPlace(date: String): LiveData<Result<List<PlaceItem>>> = liveData {
//        emit(Result.Loading)
//        try {
//            initializeApiService()
//
//            val userId = getUserId()
//            val userPreferenceId = getUserPreferenceId()
//
//            val response = apiService.getRecommendedPlace(userId, date, userPreferenceId)
//            val placeItem = response.places ?: emptyList()
//
//            emit(Result.Success(placeItem))
//        } catch (e: Exception) {
//            Log.d(TAG, "getRecommendedPlace: ${e.message}")
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    fun setPlanResult(name: String, date: String, places: List<DataPlacesItem>): LiveData<Result<PlanUserItem>> =
        liveData {
            emit(Result.Loading)
            try {
                initializeApiService()

                val userId = getUserId()
                val planUserItem = PlanUserItem(
                    name = name,
                    date = date,
                    places = places,
                    userId = userId
                )

                val response = apiService.setPlanResult(planUserItem)

                when {
                    response.data != null -> {
                        val userPlan = response.data
                        Log.d(TAG, "setPlanResult: ${response.message.toString()}")

                        emit(Result.Success(userPlan))
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
                Log.d(TAG, "setPlanResult: ${e.message}")

                emit(Result.Error(e.message.toString()))
            }
        }

    fun getPlanByUserId(): LiveData<Result<List<PlanUserItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val userId = getUserId()
            val response = apiService.getPlanByUserId(userId)

            when {
                response.data != null -> {
                    val planUserItem = response.data
                    Log.d(TAG, "getPlanByUserId: ${response.message.toString()}")

                    emit(Result.Success(planUserItem))
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
            Log.d(TAG, "getPlanByUserId: ${e.message}")

            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPlanDetailByUserId(): LiveData<Result<List<PlaceItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val userId = getUserId()
            val response = apiService.getPlanByUserId(userId)

            when {
                response.data != null -> {
                    val planUserItem = response.data

                    val places = planUserItem.flatMap { item ->
                        item.places?.map { it.place ?: PlaceItem() } ?: emptyList()
                    }
                    Log.d(TAG, "getPlanDetailByUserId: ${response.message.toString()}")

                    emit(Result.Success(places))
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
            Log.d(TAG, "getPlanDetailByUserId: ${e.message}")

            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        private const val TAG = "PlanRepository"

        private var instance: PlanRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences,
        ): PlanRepository {
            return instance ?: synchronized(this) {
                instance ?: PlanRepository(
                    apiService,
                    userPreference,
                ).also {
                    instance = it
                }
            }
        }
    }
}