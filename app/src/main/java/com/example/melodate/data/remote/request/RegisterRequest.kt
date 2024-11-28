package com.example.melodate.data.remote.request

data class RegisterRequest (
    val userName: String,
    val email: String,
    val password: String,
)