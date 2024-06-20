package com.capstonehore.ngelana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.local.database.NgelanaRoomDatabase
import com.capstonehore.ngelana.data.local.entity.Favorite

class FavoriteRepository (
    private val ngelanaRoomDatabase: NgelanaRoomDatabase,
) {

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
            emitSource(data.map { favorite ->
                Result.Success(listOf(favorite))
            })
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
        private const val TAG = "FavoriteRepository"

        private var instance: FavoriteRepository? = null

        fun getInstance(
            ngelanaRoomDatabase: NgelanaRoomDatabase
        ): FavoriteRepository {
            return instance ?: synchronized(this) {
                instance ?: FavoriteRepository(
                    ngelanaRoomDatabase
                ).also {
                    instance = it
                }
            }
        }
    }
}