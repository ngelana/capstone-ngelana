package com.capstonehore.ngelana.data.remote.response.review

import com.capstonehore.ngelana.data.remote.response.ReviewItem
import com.google.gson.annotations.SerializedName

data class ReviewResponse(

	@field:SerializedName("data")
	val data: ReviewItem? = null,

	@field:SerializedName("message")
	val message: String? = null
)
