package com.capstonehore.ngelana.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class UserInformationItem (

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        // Change the type of phone, birthdate and gender
        // add @RawValue annotation
        @field:SerializedName("phone")
        val phone: @RawValue Any? = null,

        @field:SerializedName("birthdate")
        val birthdate: @RawValue Any? = null,

        @field:SerializedName("gender")
        val gender: @RawValue Any? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("password")
        val password: String? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null
) : Parcelable
