package com.capstonehore.ngelana.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserInformationItem (

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("phone")
    val phone: Any? = null,

    @field:SerializedName("birthdate")
    val birthdate: Any? = null,

    @field:SerializedName("gender")
    val gender: Any? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)
