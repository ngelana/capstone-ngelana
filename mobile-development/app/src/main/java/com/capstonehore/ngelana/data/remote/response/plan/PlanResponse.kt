package com.capstonehore.ngelana.data.remote.response.plan

import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.google.gson.annotations.SerializedName

data class PlanResponse(

	@field:SerializedName("data")
	val data: List<UserDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataPlacesItem(

	@field:SerializedName("place")
	val place: PlaceItem? = null
)

data class UserDataItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("places")
	val places: List<DataPlacesItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)
