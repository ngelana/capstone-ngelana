package com.capstonehore.ngelana.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstonehore.ngelana.data.local.entity.Favorite

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_place")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePlace(favorite: Favorite)

    @Delete
    suspend fun deleteFavoritePlace(favorite: Favorite)
}