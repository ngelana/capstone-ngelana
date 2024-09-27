package com.capstonehore.ngelana.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlanUserItem(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("places")
    val places: List<DataPlacesItem>? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null
) : Parcelable