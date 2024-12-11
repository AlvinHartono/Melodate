package com.example.melodate.data.remote.response

import com.example.melodate.ui.dashboard.FavoriteArtistData
import com.example.melodate.ui.dashboard.TopSongData
import com.google.gson.annotations.SerializedName

data class MatchCard(
    @field:SerializedName("age")
    val age: Int,

    @field:SerializedName("biodata")
    val biodata: String?,

    @field:SerializedName("concert")
    val concert: String?,

    @field:SerializedName("date_of_birth")
    val dateOfBirth: String,

    @field:SerializedName("drinks")
    val drinks: String,

    @field:SerializedName("education")
    val education: String,

    @field:SerializedName("firstName")
    val firstName: String,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("genre")
    val genre: String,

    @field:SerializedName("height")
    val height: String,

    @field:SerializedName("hobby")
    val hobby: String,

    @field:SerializedName("listening_frequency")
    val listeningFrequency: String,

    @field:SerializedName("location")
    val location: String?,

    @field:SerializedName("love_language")

    val loveLanguage: String,
    @field:SerializedName("mbti")
    val mbti: String,

    @field:SerializedName("music_decade")
    val musicDecade: String,

    @field:SerializedName("music_vibe")
    val musicVibe: String,

    @field:SerializedName("profile_picture_url_1")
    val profilePictureUrl1: String?,

    @field:SerializedName("profile_picture_url_2")
    val profilePictureUrl2: String?,

    @field:SerializedName("profile_picture_url_3")
    val profilePictureUrl3: String?,

    @field:SerializedName("profile_picture_url_4")
    val profilePictureUrl4: String?,

    @field:SerializedName("profile_picture_url_5")
    val profilePictureUrl5: String?,

    @field:SerializedName("profile_picture_url_6")
    val profilePictureUrl6: String?,

    @field:SerializedName("religion")
    val religion: String,

    @field:SerializedName("similarity_score")
    val similarityScore: Double,

    @field:SerializedName("smokes")
    val smokes: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("user_id")
    val userId: Int,

    @field:SerializedName("topArtistImage1")
    val topArtistImage1: String? = null,

    @field:SerializedName("topArtistImage2")
    val topArtistImage2: String? = null,

    @field:SerializedName("topArtistImage3")
    val topArtistImage3: String? = null,

    @field:SerializedName("topArtistName1")
    val topArtistName1: String? = null,

    @field:SerializedName("topArtistName2")
    val topArtistName2: String? = null,

    @field:SerializedName("topArtistName3")
    val topArtistName3: String? = null,

    @field:SerializedName("topTrackImage1")
    val topTrackImage1: String? = null,

    @field:SerializedName("topTrackImage2")
    val topTrackImage2: String? = null,

    @field:SerializedName("topTrackImage3")
    val topTrackImage3: String? = null,

    @field:SerializedName("topTrackImage4")
    val topTrackImage4: String? = null,

    @field:SerializedName("topTrackImage5")
    val topTrackImage5: String? = null,

    @field:SerializedName("topTrackArtist1")
    val topTrackArtist1: String? = null,

    @field:SerializedName("topTrackArtist2")
    val topTrackArtist2: String? = null,

    @field:SerializedName("topTrackArtist3")
    val topTrackArtist3: String? = null,

    @field:SerializedName("topTrackArtist4")
    val topTrackArtist4: String? = null,

    @field:SerializedName("topTrackArtist5")
    val topTrackArtist5: String? = null,

    @field:SerializedName("topTrackTitle1")
    val topTrackTitle1: String? = null,

    @field:SerializedName("topTrackTitle2")
    val topTrackTitle2: String? = null,

    @field:SerializedName("topTrackTitle3")
    val topTrackTitle3: String? = null,

    @field:SerializedName("topTrackTitle4")
    val topTrackTitle4: String? = null,

    @field:SerializedName("topTrackTitle5")
    val topTrackTitle5: String? = null,
)