package com.capstonehore.ngelana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.ReviewItem
import com.capstonehore.ngelana.data.remote.response.review.ReviewResponse
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class ReviewRepository (
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

    fun getAllReviewByUserId(): LiveData<Result<ReviewItem>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.getAllReviewByUserId(userId)
                val reviewItem = response.data

                if (reviewItem != null) emit(Result.Success(reviewItem))
                else emit(Result.Error("Data is null"))
            } else {
                emit(Result.Error("User ID not found"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ReviewResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getReviewById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun createReview(reviewItem: ReviewItem): LiveData<Result<ReviewItem>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.createReview(reviewItem)
                val dataReview = response.data

                if (dataReview != null) emit(Result.Success(dataReview))
                else emit(Result.Error("Data is null"))
            } else {
                emit(Result.Error("User ID not found"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ReviewResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
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