package com.capstonehore.ngelana.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.local.database.NgelanaRoomDatabase
import com.capstonehore.ngelana.data.local.entity.Favorite
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.data.remote.response.ReviewItem
import com.capstonehore.ngelana.data.remote.response.UserInformationItem
import com.capstonehore.ngelana.data.remote.response.preferences.UserDataPreferencesItem
import com.capstonehore.ngelana.data.remote.response.users.LoginModel
import com.capstonehore.ngelana.data.remote.response.users.LoginResponse
import com.capstonehore.ngelana.data.remote.response.users.RegisterModel
import com.capstonehore.ngelana.data.remote.response.users.RegisterResponse
import com.capstonehore.ngelana.data.remote.response.users.UserResponse
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import java.util.Locale

class Repository (
    private var apiService: ApiService,
    private val userPreferences: UserPreferences,
    private val ngelanaRoomDatabase: NgelanaRoomDatabase
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


    // Users
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


    // Places
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


    // Plan
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


    // Preferences
    fun getAllPreferences(): LiveData<Result<List<PreferenceItem>>> = liveData {
        emit(Result.Loading)
        try {
            initializeApiService()

            val response = apiService.getAllPreferences()
            val preferenceItem = response.data ?: emptyList()

            emit(Result.Success(preferenceItem))
        } catch (e: Exception) {
            Log.d(TAG, "getUserPreferencesById: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

//    fun createUserPreference(userDataPreferencesItems: List<UserDataPreferencesItem>):
//            LiveData<Result<List<UserDataPreferencesItem>>> = liveData {
//        emit(Result.Loading)
//        try {
//            initializeApiService()
//
//            val response = apiService.createUserPreference(userDataPreferencesItems)
//            val userDataPreferences = response.data ?: emptyList()
//
//            emit(Result.Success(userDataPreferences))
//        } catch (e: Exception) {
//            Log.d(TAG, "createUserPreference: ${e.message}")
//            emit(Result.Error(e.message.toString()))
//        }
//    }

//    fun getPreferenceByUserId(): LiveData<Result<List<PreferenceItem>>> = liveData {
//        emit(Result.Loading)
//        try {
//            initializeApiService()
//
//            val userId = getUserId()
//            val response = apiService.getPreferenceByUserId(userId)
//            val userPreference = response.user
//            val userPreferenceItem = userPreference?.userPreferences ?: emptyList()
//
//            emit(Result.Success(userPreferenceItem.map { it.preference ?: PreferenceItem() }))
//        } catch (e: Exception) {
//            Log.d(TAG, "getPreferenceById: ${e.message}")
//            emit(Result.Error(e.message.toString()))
//        }
//    }

//    fun updateUserPreference(preferenceItem: List<PreferenceItem>): LiveData<Result<List<PreferenceItem>>> = liveData {
//        emit(Result.Loading)
//        try {
//            initializeApiService()
//
//            val userId = getUserId()
//            val response = apiService.updateUserPreference(userId, preferenceItem)
//            val updatedPreferences = response.data ?: emptyList()
//
//            emit(Result.Success(updatedPreferences.map { it.preference ?: PreferenceItem() }))
//        } catch (e: Exception) {
//            Log.d(TAG, "updateUserPreference: ${e.message}")
//            emit(Result.Error(e.message.toString()))
//        }
//    }


    // Review
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


    // Local
    fun getAllFavorites(): LiveData<Result<List<Favorite>>> = liveData {
        emit(Result.Loading)
        try {
            val response = ngelanaRoomDatabase.favoriteDao().getAllFavorites()
            val data = response.value ?: emptyList()

            if (data.isNotEmpty()) {
                emit(Result.Success(data.filterNotNull()))
            } else {
                emit(Result.Error("Favorites not found or empty"))
            }
        } catch (e: Exception) {
            Log.d(TAG, "getAllFavorites: ${e.message}")
            emit(Result.Error("Error fetching data: ${e.message}"))
        }
    }

    fun getFavoriteByPlaceId(placeId: String): LiveData<Result<List<Favorite>>> = liveData {
        emit(Result.Loading)
        try {
            val response = ngelanaRoomDatabase.favoriteDao().getFavoriteByPlaceId(placeId)
            val data = response.value

            if (data != null) {
                emit(Result.Success(listOf(data)))
            } else {
                emit(Result.Error("Favorite not found"))
            }
        } catch (e: Exception) {
            Log.d(TAG, "getFavoriteByPlaceId: ${e.message}")
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


    companion object {
        private const val TAG = "Repository"

        private var instance: Repository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences,
            ngelanaRoomDatabase: NgelanaRoomDatabase
        ): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository(
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