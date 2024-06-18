package com.capstonehore.ngelana.data.remote.response.preferences

import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.google.gson.annotations.SerializedName

data class PreferencesResponse(
    @SerializedName("data")
    val data: List<PreferenceItem>? = null
)
