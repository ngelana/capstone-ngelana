package com.capstonehore.ngelana.data.remote.response.review

import com.google.gson.annotations.SerializedName

data class ReviewResponse(

	@field:SerializedName("data")
	val data: ReviewItem? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ReviewItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("star")
	val star: Int? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("placeId")
	val placeId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)
