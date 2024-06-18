package com.capstonehore.ngelana.data.remote.response.places

import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.google.gson.annotations.SerializedName

data class PlacesResponse(

	@field:SerializedName("data")
	val data: List<PlaceItem?>? = null
)
