package com.example.melodate.data.repository

import com.example.melodate.data.remote.response.MatchCard
import com.example.melodate.data.remote.retrofit.ApiService
import com.example.melodate.data.Result

class MatchRepository(private val apiService: ApiService) {

    suspend fun getRecommendations(userId: String): Result<List<MatchCard>> {
        return try {
            val response = apiService.getRecommendations("user$userId")
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error fetching recommendations")
        }
    }
}
