package com.capstonehore.ngelana.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PreferenceItem(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("urlPlaceholder")
    val urlPlaceholder: List<String>? = null
) : Parcelable
