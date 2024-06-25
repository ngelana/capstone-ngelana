package com.capstonehore.ngelana.data.remote.response.preferences

import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.google.gson.annotations.SerializedName

data class PreferencesResponseByUserId(

	@field:SerializedName("message")
	val message: String? = null,

	@SerializedName("data")
	val data: PreferencesData?,
)

data class PreferencesData(
	@SerializedName("userPreferences")
	val userPreferences: List<UserPreferencesItem>?,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class UserPreferencesItem(

	@field:SerializedName("preference")
	val preference: PreferenceItem? = null
)
