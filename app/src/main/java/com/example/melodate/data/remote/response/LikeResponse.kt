package com.example.melodate.data.remote.response

import com.google.gson.annotations.SerializedName

data class LikeResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class Data(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("is_matched")
	val isMatched: Boolean? = null,

	@field:SerializedName("user_2")
	val user2: Int? = null,

	@field:SerializedName("user_1")
	val user1: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("match_date")
	val matchDate: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
