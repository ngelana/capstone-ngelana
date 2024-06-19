package com.capstonehore.ngelana.data.remote.response.places

import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.google.gson.annotations.SerializedName

data class PlaceResponseById(

	@field:SerializedName("data")
	val data: PlaceItem? = null,

	@field:SerializedName("message")
	val message: String? = null
)
