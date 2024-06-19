package com.capstonehore.ngelana.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstonehore.ngelana.data.local.entity.Favorite

@Database(entities = [Favorite::class], version = 2)
abstract class NgelanaRoomDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: NgelanaRoomDatabase? = null

        fun getDatabase(context: Context): NgelanaRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NgelanaRoomDatabase::class.java,
                    "favorite-place.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}