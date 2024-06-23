package com.capstonehore.ngelana.data.remote.response

import com.google.gson.annotations.SerializedName

data class PlanUserItem(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("places")
    val places: List<DataPlacesItem>? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null
)