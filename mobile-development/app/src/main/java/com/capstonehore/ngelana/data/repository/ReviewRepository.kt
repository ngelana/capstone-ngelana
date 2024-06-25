package com.capstonehore.ngelana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.ReviewItem
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first

class ReviewRepository (
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

    fun getAllReviewByUserId(): LiveData<Result<List<ReviewItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val userId = getUserId()
            val response = apiService.getAllReviewByUserId(userId)
            val reviewItem = response.data

            if (reviewItem != null) emit(Result.Success(reviewItem))
            else emit(Result.Error("Data is null"))
        } catch (e: Exception) {
            Log.d(TAG, "getReviewById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun createReview(reviewItem: ReviewItem): LiveData<Result<List<ReviewItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val response = apiService.createReview(reviewItem)
            val dataReview = response.data

            if (dataReview != null) emit(Result.Success(dataReview))
            else emit(Result.Error("Data is null"))
        } catch (e: Exception) {
            Log.d(TAG, "createReview: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        private const val TAG = "ReviewRepository"

        private var instance: ReviewRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences,
        ): ReviewRepository {
            return instance ?: synchronized(this) {
                instance ?: ReviewRepository(
                    apiService,
                    userPreference,
                ).also {
                    instance = it
                }
            }
        }
    }
}