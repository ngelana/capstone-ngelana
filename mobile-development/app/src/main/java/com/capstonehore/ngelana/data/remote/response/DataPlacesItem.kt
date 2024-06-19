package com.capstonehore.ngelana.data.remote.response

import com.google.gson.annotations.SerializedName

data class DataPlacesItem(

    @field:SerializedName("place")
    val place: PlaceItem? = null,

    @field:SerializedName("urlPlaceholder")
    val urlPlaceholder: List<String?>? = null
)