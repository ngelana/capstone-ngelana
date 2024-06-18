package com.capstonehore.ngelana.data.remote.response.preferences

import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.google.gson.annotations.SerializedName

data class PreferencesResponseById(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: UserPreference? = null
)

data class UserPreference(

	@field:SerializedName("userPreferences")
	val userPreferences: List<UserPreferencesItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class UserPreferencesItem(

	@field:SerializedName("preference")
	val preference: PreferenceItem? = null
)