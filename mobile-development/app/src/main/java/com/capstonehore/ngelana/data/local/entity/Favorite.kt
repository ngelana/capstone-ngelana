package com.capstonehore.ngelana.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_place")
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = false)

    @field:ColumnInfo(name = "place_name")
    var placeName: String = "",

    @field:ColumnInfo(name = "place_image")
    var placeImage: String? = null
) : Parcelable