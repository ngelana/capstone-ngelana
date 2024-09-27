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
    @field:ColumnInfo(name = "place_id")
    var placeId: String = "",

    @field:ColumnInfo(name = "place_name")
    var placeName: String? = null,

    @field:ColumnInfo(name = "place_image")
    var placeImage: String? = null,

    @field:ColumnInfo(name = "place_city")
    var placeCity: String? = null,

    @field:ColumnInfo(name = "place_rating")
    var placeRating: String? = null,

    @field:ColumnInfo(name = "place_type")
    var placeType: String? = null
) : Parcelable