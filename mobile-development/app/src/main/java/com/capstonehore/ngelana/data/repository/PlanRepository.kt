package com.capstonehore.ngelana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.DataPlacesItem
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.data.remote.response.ReviewItem
import com.capstonehore.ngelana.data.remote.response.plan.PlaceRecommendedResponse
import com.capstonehore.ngelana.data.remote.response.plan.PlanResponse
import com.capstonehore.ngelana.data.remote.response.plan.PlanResultResponse
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class PlanRepository (
    private var apiService: ApiService,
    private val userPreferences: UserPreferences,
) {

    private var token: String? = null
    private var userId: String? = null
    private var userPreferenceId: String? = null

    private suspend fun getToken(): String? = token ?: runBlocking {
        userPreferences.getToken().first()
    }.also { token = it }

    private suspend fun getUserId(): String? =
        userId ?: userPreferences.getUserId().first().also { userId = it }

    private suspend fun getUserPreferenceId(): String? =
        userPreferenceId ?: userPreferences.getUserPreferenceId().first()
            .also { userPreferenceId = it }

    fun getRecommendedPlace(date: String): LiveData<Result<List<PlaceItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()
            val userPreferenceId = getUserPreferenceId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null && userPreferenceId != null) {
                val response = apiService.getRecommendedPlace(userId, date, userPreferenceId)
                val placeItem = response.places ?: emptyList()
                emit(Result.Success(placeItem))
            } else {
                emit(Result.Error("User ID or User Preference ID not found"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PlaceRecommendedResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getRecommendedPlace: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun setPlanResult(planUserItem: PlanUserItem): LiveData<Result<PlanUserItem>> =
        liveData {
            emit(Result.Loading)
            try {
                val token = getToken()
                val userId = getUserId()

                apiService = ApiConfig.getApiService(token.toString())
                if (userId != null) {
                    val response = apiService.setPlanResult(planUserItem)
                    val userPlan = response.data

                    if (userPlan != null) emit(Result.Success(userPlan))
                    else emit(Result.Error("Data is null"))
                } else {
                    emit(Result.Error("User ID not found"))
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, PlanResultResponse::class.java)

                emit(Result.Error(errorResponse.message.toString()))
            } catch (e: Exception) {
                Log.d(TAG, "setPlanResult: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun setDetailPlanResult(planUserItem: PlanUserItem): LiveData<Result<List<PlaceItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.setPlanResult(planUserItem)
                val dataPlanUser = response.data?.places

                val places = dataPlanUser?.map { item -> item.place ?: PlaceItem() } ?: emptyList()
                emit(Result.Success(places))
            } else {
                emit(Result.Error("User ID not found"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PlanResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getPlanByUserId: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPlanByUserId(): LiveData<Result<List<PlanUserItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.getPlanByUserId(userId)
                val planUserItem = response.data ?: emptyList()
                emit(Result.Success(planUserItem))
            } else {
                emit(Result.Error("User ID not found"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PlanResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getPlanByUserId: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPlanDetailByUserId(): LiveData<Result<List<PlaceItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.getPlanByUserId(userId)
                val planUserItem = response.data ?: emptyList()

                val places = planUserItem.flatMap { item ->
                    item.places?.map { it.place ?: PlaceItem() } ?: emptyList()
                }

                emit(Result.Success(places))
            } else {
                emit(Result.Error("User ID not found"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PlanResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getPlanByUserId: ${e.message}")
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