package com.capstonehore.ngelana.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.data.remote.response.places.PlaceResponseById
import com.capstonehore.ngelana.data.remote.response.places.PlacesResponse
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import com.capstonehore.ngelana.data.repository.Repository.Companion
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.util.Locale

class PlaceRepository (
    private var apiService: ApiService,
    private val userPreferences: UserPreferences,
) {

    private var token: String? = null

    private suspend fun getToken(): String {
        if (token.isNullOrEmpty()) {
            token = userPreferences.getToken().first()
        }
        return token ?: ""
    }

    private suspend fun initializeApiService() {
        val token = getToken()
        apiService = ApiConfig.getApiService(token)
    }

    fun getAllPlaces(): LiveData<Result<List<PlaceItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val response = apiService.getAllPlaces()
            val placeItem = response.data ?: emptyList()

            emit(Result.Success(placeItem))
        } catch (e: Exception) {
            Log.d(TAG, "getAllPlaces: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPlaceById(id: String): LiveData<Result<PlaceItem>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val response = apiService.getPlaceById(id)
            val placeItem = response.data

            if (placeItem != null) emit(Result.Success(placeItem))
            else emit(Result.Error("Data is null"))
        } catch (e: Exception) {
            Log.d(TAG, "getPlaceById: ${e.message}")
            emit(Result.Error("Unexpected error: ${e.message}"))
        }
    }

    fun searchPlaceByQuery(query: String): LiveData<Result<List<PlaceItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val response = apiService.searchPlaceByQuery(query)
            val placeItem = response.data ?: emptyList()

            emit(Result.Success(placeItem))
        } catch (e: Exception) {
            Log.d(TAG, "searchPlaceByQuery: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPrimaryTypePlace(type: String): LiveData<Result<List<PlaceItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val response = apiService.getPrimaryTypePlace(type)
            val placeItem = response.data ?: emptyList()

            emit(Result.Success(placeItem))
        } catch (e: Exception) {
            Log.d(TAG, "getPrimaryTypePlace: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getLocationDetails(context: Context, location: Location): LiveData<Result<Address>> =
        liveData {
            emit(Result.Loading)
            try {
                val geocoder = Geocoder(context, Locale.getDefault())

                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    emit(Result.Success(addresses[0]))
                } else {
                    emit(Result.Error("No address found"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "getLocationDetails: ${e.message}", e)
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }

    companion object {
        private const val TAG = "PlaceRepository"

        private var instance: PlaceRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences,
        ): PlaceRepository {
            return instance ?: synchronized(this) {
                instance ?: PlaceRepository(
                    apiService,
                    userPreference,
                ).also {
                    instance = it
                }
            }
        }
    }
}