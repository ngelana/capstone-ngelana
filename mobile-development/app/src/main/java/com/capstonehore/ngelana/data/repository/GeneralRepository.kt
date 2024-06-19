package com.capstonehore.ngelana.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.local.database.NgelanaRoomDatabase
import com.capstonehore.ngelana.data.local.entity.Favorite
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.data.remote.response.ReviewItem
import com.capstonehore.ngelana.data.remote.response.UserInformationItem
import com.capstonehore.ngelana.data.remote.response.places.PlaceResponseById
import com.capstonehore.ngelana.data.remote.response.places.PlacesResponse
import com.capstonehore.ngelana.data.remote.response.plan.PlanResponse
import com.capstonehore.ngelana.data.remote.response.preferences.PreferencesResponse
import com.capstonehore.ngelana.data.remote.response.preferences.PreferencesResponseByUserId
import com.capstonehore.ngelana.data.remote.response.preferences.UserDataPreferencesItem
import com.capstonehore.ngelana.data.remote.response.preferences.UserPreferenceResponse
import com.capstonehore.ngelana.data.remote.response.preferences.UserPreferencesItem
import com.capstonehore.ngelana.data.remote.response.review.ReviewResponse
import com.capstonehore.ngelana.data.remote.response.users.LoginResponse
import com.capstonehore.ngelana.data.remote.response.users.RegisterResponse
import com.capstonehore.ngelana.data.remote.response.users.UserResponse
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.util.Locale

class GeneralRepository(
    private var apiService: ApiService,
    private val userPreferences: UserPreferences,
    private val ngelanaRoomDatabase: NgelanaRoomDatabase,
) {

    private var token: String? = null
    private var userId: String? = null

    // Data Remote
    private suspend fun getToken(): String? = token ?: runBlocking {
        userPreferences.getToken().first()
    }.also { token = it }

    private suspend fun getUserId(): String? =
        userId ?: userPreferences.getUserId().first().also { userId = it }

    // Users
    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)

            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)

            emit(Result.Error(errorResponse.message.toString()))
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
            val response = apiService.login(email, password)

            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)

            emit(Result.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "login  : ${e.message}")

            emit(Result.Error(e.message.toString()))
        }
    }

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

    fun updateUserById(userInformationItem: UserInformationItem): LiveData<Result<UserResponse>> = liveData {
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


    // Places
    fun getPlaceById(id: String): LiveData<Result<PlaceItem>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            apiService = ApiConfig.getApiService(token.toString())

            val response = apiService.getPlaceById(id)
            val placeItem = response.data

            if (placeItem != null) {
                emit(Result.Success(placeItem))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PlaceResponseById::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getPlaceById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun searchPlaceByQuery(query: String): LiveData<Result<List<PlaceItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            apiService = ApiConfig.getApiService(token.toString())

            val response = apiService.searchPlaceByQuery(query)
            val places = response.data?.filterNotNull() ?: emptyList()

            emit(Result.Success(places))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PlacesResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "searchPlaceByQuery: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPrimaryTypePlace(type: String): LiveData<Result<List<PlaceItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            apiService = ApiConfig.getApiService(token.toString())

            val response = apiService.getPrimaryTypePlace(type)
            val places = response.data?.filterNotNull() ?: emptyList()

            emit(Result.Success(places))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PlacesResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getPrimaryTypePlace: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }


    // Plan
    fun getPlanByUserId(): LiveData<Result<PlanResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.getPlanByUserId(userId)
                emit(Result.Success(response))
            } else {
                emit(Result.Error("User ID tidak ditemukan"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PlacesResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getPlanByUserId: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }


    // Preferences
    fun getAllPreferences(): LiveData<Result<PreferencesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            apiService = ApiConfig.getApiService(token.toString())

            val response = apiService.getAllPreferences()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UserPreferencesItem::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getUserPreferencesById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun createPreference(userDataPreferencesItem: UserDataPreferencesItem): LiveData<Result<UserPreferenceResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.createPreference(userDataPreferencesItem)
                emit(Result.Success(response))
            } else {
                emit(Result.Error("User ID tidak ditemukan"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UserPreferenceResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "createPreference: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPreferenceById(id: String): LiveData<Result<PreferencesResponseByUserId>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            apiService = ApiConfig.getApiService(token.toString())

            val response = apiService.getPreferenceByUserId(id)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PreferencesResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getPreferenceById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    // Review
    fun getAllReviewByUserId(): LiveData<Result<ReviewResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.getAllReviewByUserId(userId)
                emit(Result.Success(response))
            } else {
                emit(Result.Error("User ID tidak ditemukan"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PreferencesResponse::class.java)

            emit(Result.Error(errorResponse.toString()))
        } catch (e: Exception) {
            Log.d(TAG, "getReviewById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun createReview(reviewItem: ReviewItem): LiveData<Result<ReviewResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = getToken()
            val userId = getUserId()

            apiService = ApiConfig.getApiService(token.toString())
            if (userId != null) {
                val response = apiService.createReview(reviewItem)
                emit(Result.Success(response))
            } else {
                emit(Result.Error("User ID tidak ditemukan"))
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

    // Data Local
    fun getAllFavorites(): LiveData<Result<List<Favorite>>> = liveData {
        emit(Result.Loading)
        try {
            val data = ngelanaRoomDatabase.favoriteDao().getAllFavorites()
            emitSource(data.map { Result.Success(it) })
        } catch (e: Exception) {
            Log.d(TAG, "getAllFavorites: ${e.message}")
            emit(Result.Error("Error fetching data: ${e.message}"))
        }
    }

    fun getFavoriteByPlaceId(placeId: String): LiveData<Result<List<Favorite>>> = liveData {
        emit(Result.Loading)
        try {
            val data = ngelanaRoomDatabase.favoriteDao().getFavoriteByPlaceId(placeId)

            val job = CoroutineScope(Dispatchers.IO).launch {
                data.value?.let { favorite ->
                    val listFavorite = listOf(favorite)
                    emit(Result.Success(listFavorite))
                }
            }
            job.join()
        } catch (e: Exception) {
            Log.d(TAG, "getFavoriteByPlaceName: ${e.message}")
            emit(Result.Error("Error fetching data: ${e.message}"))
        }
    }

    fun insertFavoritePlace(favorite: Favorite): LiveData<Result<Unit>> = liveData {
        emit(Result.Loading)
        try {
            val data = ngelanaRoomDatabase.favoriteDao().insertFavoritePlace(favorite)
            emit(Result.Success(data))
        } catch (e: Exception) {
            Log.d(TAG, "insertFavoritePlace: ${e.message}")
            emit(Result.Error("Error inserting data: ${e.message}"))
        }
    }

    fun deleteFavoritePlace(favorite: Favorite): LiveData<Result<Unit>> = liveData {
        emit(Result.Loading)
        try {
            val data = ngelanaRoomDatabase.favoriteDao().deleteFavoritePlace(favorite)
            emit(Result.Success(data))
        } catch (e: Exception) {
            Log.d(TAG, "deleteFavoritePlace: ${e.message}")
            emit(Result.Error("Error deleting data: ${e.message}"))
        }
    }

    // Data Location
    fun getLocationDetails(context: Context, location: Location): LiveData<Result<Address>> =
        liveData {
            emit(Result.Loading)
            try {
                val geocoder = Geocoder(context, Locale.getDefault())

                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses != null) {
                    if (addresses.isNotEmpty()) {
                        emit(Result.Success(addresses[0]))
                    } else {
                        emit(Result.Error("No address found"))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "getLocationDetails: ${e.message}", e)
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }

    companion object {
        private const val TAG = "GeneralRepository"

        private var instance: GeneralRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences,
            ngelanaRoomDatabase: NgelanaRoomDatabase,
        ): GeneralRepository {
            return instance ?: synchronized(this) {
                instance ?: GeneralRepository(
                    apiService,
                    userPreference,
                    ngelanaRoomDatabase
                ).also {
                    instance = it
                }
            }
        }
    }
}