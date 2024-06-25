package com.capstonehore.ngelana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
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

    fun setPlanResult(planUserItem: PlanUserItem): LiveData<Result<PlanUserItem>> =
        liveData {
            emit(Result.Loading)
            try {
                initializeApiService()

                val response = apiService.setPlanResult(planUserItem)
                val userPlan = response.data

                if (userPlan != null) emit(Result.Success(userPlan))
                else emit(Result.Error("Data is null"))
            } catch (e: Exception) {
                Log.d(TAG, "setPlanResult: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun setDetailPlanResult(planUserItem: PlanUserItem): LiveData<Result<List<PlaceItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val response = apiService.setPlanResult(planUserItem)
            val dataPlanUser = response.data?.places
            val places = dataPlanUser?.map { item -> item.place ?: PlaceItem() } ?: emptyList()

            emit(Result.Success(places))
        } catch (e: Exception) {
            Log.d(TAG, "getPlanByUserId: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPlanByUserId(): LiveData<Result<List<PlanUserItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val userId = getUserId()
            val response = apiService.getPlanByUserId(userId)
            val planUserItem = response.data ?: emptyList()

            emit(Result.Success(planUserItem))
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
            val planUserItem = response.data ?: emptyList()

            val places = planUserItem.flatMap { item ->
                item.places?.map { it.place ?: PlaceItem() } ?: emptyList()
            }

            emit(Result.Success(places))
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