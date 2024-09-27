package com.capstonehore.ngelana.data.remote.response.plan

import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.google.gson.annotations.SerializedName

data class PlaceRecommendedResponse(

	@field:SerializedName("places")
	val places: List<PlaceItem>? = null,

	@field:SerializedName("message")
	val message: String? = null
)
