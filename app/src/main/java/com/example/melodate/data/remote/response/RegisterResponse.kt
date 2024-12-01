package com.example.melodate.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

	@field:SerializedName("education")
	val education: String? = null,

	@field:SerializedName("drinking")
	val drinking: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("music_decade")
	val musicDecade: String? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("concert")
	val concert: String? = null,

	@field:SerializedName("love_language")
	val loveLanguage: String? = null,

	@field:SerializedName("religion")
	val religion: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("relationship_status")
	val relationshipStatus: String? = null,

	@field:SerializedName("smoking")
	val smoking: String? = null,

	@field:SerializedName("genre")
	val genre: String? = null,

	@field:SerializedName("mbti")
	val mbti: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("listening_frequency")
	val listeningFrequency: String? = null,

	@field:SerializedName("hobby")
	val hobby: String? = null,

	@field:SerializedName("height")
	val height: String? = null
)
