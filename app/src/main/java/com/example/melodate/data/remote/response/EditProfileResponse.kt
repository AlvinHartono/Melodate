package com.example.melodate.data.remote.response

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: EditProfileUser? = null
)

data class EditProfileUser(

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

	@field:SerializedName("topArtistName1")
	val topArtistName1: String? = null,

	@field:SerializedName("topTrackTitle2")
	val topTrackTitle2: String? = null,

	@field:SerializedName("topArtistName2")
	val topArtistName2: String? = null,

	@field:SerializedName("topTrackTitle3")
	val topTrackTitle3: String? = null,

	@field:SerializedName("topTrackTitle4")
	val topTrackTitle4: String? = null,

	@field:SerializedName("topTrackTitle5")
	val topTrackTitle5: String? = null,

	@field:SerializedName("love_language")
	val loveLanguage: String? = null,

	@field:SerializedName("topArtistName3")
	val topArtistName3: String? = null,

	@field:SerializedName("religion")
	val religion: String? = null,

	@field:SerializedName("topTrackArtist2")
	val topTrackArtist2: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("topTrackArtist1")
	val topTrackArtist1: String? = null,

	@field:SerializedName("topTrackArtist4")
	val topTrackArtist4: String? = null,

	@field:SerializedName("topTrackArtist3")
	val topTrackArtist3: String? = null,

	@field:SerializedName("topTrackTitle1")
	val topTrackTitle1: String? = null,

	@field:SerializedName("profilePicture5")
	val profilePicture5: Any? = null,

	@field:SerializedName("mbti")
	val mbti: String? = null,

	@field:SerializedName("profilePicture6")
	val profilePicture6: Any? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("topArtistImage3")
	val topArtistImage3: String? = null,

	@field:SerializedName("topArtistImage1")
	val topArtistImage1: String? = null,

	@field:SerializedName("user")
	val user: Int? = null,

	@field:SerializedName("topArtistImage2")
	val topArtistImage2: String? = null,

	@field:SerializedName("topTrackArtist5")
	val topTrackArtist5: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("listening_frequency")
	val listeningFrequency: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("hobby")
	val hobby: String? = null
)
