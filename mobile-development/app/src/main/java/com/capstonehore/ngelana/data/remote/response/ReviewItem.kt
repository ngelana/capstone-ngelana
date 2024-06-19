package com.capstonehore.ngelana.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewItem(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("star")
    val star: Int? = null,

    @field:SerializedName("review")
    val review: String? = null,

    @field:SerializedName("placeId")
    val placeId: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null
): Parcelable