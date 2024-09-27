package com.capstonehore.ngelana.data.remote.response.preferences

import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.google.gson.annotations.SerializedName

data class UserPreferenceResponse(

	@field:SerializedName("data")
	val data: List<UserDataPreferencesItem>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UserDataPreferencesItem(

	@SerializedName("preference")
	val preference: PreferenceItem? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)
