package com.capstonehore.ngelana.data.remote.response.plan

import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.google.gson.annotations.SerializedName

data class PlanResultResponse(

	@field:SerializedName("data")
	val data: PlanUserItem? = null,

	@field:SerializedName("message")
	val message: String? = null
)
