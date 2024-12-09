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

    val favoriteArtists: List<FavoriteArtistData>,
    val topSongs: List<TopSongData>
)