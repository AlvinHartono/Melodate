package com.example.melodate.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetUserDataResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: GetUserData? = null
)

data class GetUserData(

	@field:SerializedName("education")
	val education: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("profilePicture1")
	val profilePicture1: String? = null,

	@field:SerializedName("profilePicture2")
	val profilePicture2: String? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("profilePicture3")
	val profilePicture3: String? = null,

	@field:SerializedName("drinks")
	val drinks: String? = null,

	@field:SerializedName("profilePicture4")
	val profilePicture4: String? = null,

	@field:SerializedName("concert")
	val concert: String? = null,

	@field:SerializedName("music_vibe")
	val musicVibe: String? = null,

	@field:SerializedName("smokes")
	val smokes: String? = null,

	@field:SerializedName("genre")
	val genre: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("height")
	val height: String? = null,

	@field:SerializedName("music_decade")
	val musicDecade: String? = null,

	@field:SerializedName("biodata")
	val biodata: Any? = null,

	@field:SerializedName("love_language")
	val loveLanguage: String? = null,

	@field:SerializedName("religion")
	val religion: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("profilePicture5")
	val profilePicture5: String? = null,

	@field:SerializedName("mbti")
	val mbti: String? = null,

	@field:SerializedName("profilePicture6")
	val profilePicture6: String? = null,

	@field:SerializedName("location")
	val location: Any? = null,

	@field:SerializedName("user")
	val user: Int? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("listening_frequency")
	val listeningFrequency: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("hobby")
	val hobby: String? = null
)
