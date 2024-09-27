package com.capstonehore.ngelana.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataPlacesItem(

    @field:SerializedName("place")
    val place: PlaceItem? = null,
) : Parcelable