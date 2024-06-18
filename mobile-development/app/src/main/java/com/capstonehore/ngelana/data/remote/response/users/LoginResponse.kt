package com.capstonehore.ngelana.data.remote.response.users

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
	val data: DataLogin? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("token")
	val token: String? = null
)

data class DataLogin(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
