package com.example.melodate.data.remote.retrofit

import com.example.melodate.data.remote.request.CheckEmailRequest
import com.example.melodate.data.remote.request.LoginRequest
import com.example.melodate.data.remote.request.RegisterRequest
import com.example.melodate.data.remote.response.CheckEmailResponse
import com.example.melodate.data.remote.response.LoginResponse
import com.example.melodate.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // Authentication
    @POST("/api/users/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("/api/users/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("/api/users/checkEmail")
    suspend fun checkEmail(
        @Body request: CheckEmailRequest
    ):CheckEmailResponse
}