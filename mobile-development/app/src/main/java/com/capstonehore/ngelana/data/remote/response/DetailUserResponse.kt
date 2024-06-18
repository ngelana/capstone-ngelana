package com.capstonehore.ngelana.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

	@field:SerializedName("data")
	val data: UserInformationItem? = null,

	@field:SerializedName("message")
	val message: String? = null
)
