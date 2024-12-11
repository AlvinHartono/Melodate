package com.example.melodate.data.remote.response

import com.google.gson.annotations.SerializedName

data class MatchesListResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>
)

data class UserTwo(

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class UserOne(

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class DataItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("userOne")
	val userOne: UserOne? = null,

	@field:SerializedName("is_matched")
	val isMatched: Boolean? = null,

	@field:SerializedName("user_2")
	val user2: Int? = null,

	@field:SerializedName("user_1")
	val user1: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userTwo")
	val userTwo: UserTwo? = null,

	@field:SerializedName("match_date")
	val matchDate: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
