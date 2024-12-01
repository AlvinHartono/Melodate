package com.example.melodate.data.remote.request

import java.io.File

data class RegisterRequest(
    val email: String,
    val password: String,
    val confirmPassword: String,
    val firstName: String,
    val date_of_birth: String,
    val gender: String,
    val relationship_status: String,
    val education: String,
    val religion: String,
    val hobby: String,
    val height: Int,
    val smoking: String,
    val drinking: String,
    val mbti: String,
    val love_language: String,
    val genre: String,
    val music_decade: String,
    val listening_frequency: String,
    val concert: String,
    val music_vibe: String, //kurang
    val profilePictureUrls: File,


    val bio: String,

)