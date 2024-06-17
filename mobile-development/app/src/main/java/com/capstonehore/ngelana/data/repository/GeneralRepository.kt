package com.capstonehore.ngelana.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.local.database.NgelanaRoomDatabase
import com.capstonehore.ngelana.data.local.entity.Favorite
import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.LoginResponse
import com.capstonehore.ngelana.data.remote.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.util.Locale

class GeneralRepository(
        private var apiService: ApiService,
        private val userPreferences: UserPreferences,
        private val ngelanaRoomDatabase: NgelanaRoomDatabase,
) {

    private var token: String? = null

    private suspend fun getToken(): String? = token ?: runBlocking {
        userPreferences.getToken().first()
    }.also { token = it }

    fun register(
            name: String,
            email: String,
            password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "register info : ${e.message}")
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun login(
            email: String,
            password: String,
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            val loginResult = response.data

            if (loginResult != null) {
                emit(Result.Success(response)) //if sukses
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            Log.e("Login Server error", "Login HTTP : ${e.message}")
            emit(Result.Error(errorResponse.message.toString()))

        } catch (e: Exception) {
            Log.d("Login Result error :", "Login  : ${e.message}")
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

    fun insertFavoritePlace(favorite: Favorite): LiveData<Result<Unit>> = liveData {
        emit(Result.Loading)
        try {
            ngelanaRoomDatabase.favoriteDao().insertFavoritePlace(favorite)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            Log.d(TAG, "insertFavoritePlace: ${e.message}")
            emit(Result.Error("Error inserting data: ${e.message}"))
        }
    }

    fun deleteFavoritePlace(favorite: Favorite): LiveData<Result<Unit>> = liveData {
        emit(Result.Loading)
        try {
            ngelanaRoomDatabase.favoriteDao().deleteFavoritePlace(favorite)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            Log.d(TAG, "deleteFavoritePlace: ${e.message}")
            emit(Result.Error("Error deleting data: ${e.message}"))
        }
    }

    // Data Location
    fun getLocationDetails(context: Context, location: Location): LiveData<Result<Address>> = liveData {
        emit(Result.Loading)
        try {
            val geocoder = Geocoder(context, Locale.getDefault())

            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    emit(Result.Success(addresses[0]))
                }
                else {
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
                instance ?: GeneralRepository(apiService, userPreference, ngelanaRoomDatabase).also {
                    instance = it
                }
            }
        }
    }
}