package com.capstonehore.ngelana.data.remote.response.preferences

import com.google.gson.annotations.SerializedName

data class PreferenceModel(
    @SerializedName("preferenceIds")
    val preferenceIds: List<String>? = null,

    @SerializedName("userId")
    val userId: String? = null
)