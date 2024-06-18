package com.capstonehore.ngelana.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInformationItem (

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("phone")
        val phone: String? = null,

        @field:SerializedName("birthdate")
        val birthdate: String? = null,

        @field:SerializedName("gender")
        val gender: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("password")
        val password: String? = null,
) : Parcelable
