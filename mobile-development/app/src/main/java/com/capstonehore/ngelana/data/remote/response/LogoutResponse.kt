package com.capstonehore.ngelana.data.remote.response

import com.google.gson.annotations.SerializedName

data class LogoutResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
