package com.capstonehore.ngelana.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceItem(

    @field:SerializedName("types")
    val types: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("latitude")
    val latitude: Double? = null,

    @field:SerializedName("rating")
    val rating: Double? = null,

    @field:SerializedName("primaryTypes")
    val primaryTypes: String? = null,

    @field:SerializedName("ratingCount")
    val ratingCount: Int? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("longitude")
    val longitude: Double? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("urlPlaceholder")
    val urlPlaceholder: List<String>? = null
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaceItem

        return id == other.id
    }

    override fun hashCode(): Int {
        val result = id.hashCode()
        return result
    }
}