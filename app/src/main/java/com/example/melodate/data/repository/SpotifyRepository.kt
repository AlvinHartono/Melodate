package com.example.melodate.data.repository

import com.example.melodate.data.Result
import com.example.melodate.data.remote.response.SpotifyTopArtistsResponse
import com.example.melodate.data.remote.response.SpotifyTopTracksResponse
import com.example.melodate.data.remote.retrofit.ApiService

class SpotifyRepository(private val apiService: ApiService) {

    suspend fun getTopArtists(token: String): Result<SpotifyTopArtistsResponse> {
        return try {
            val response = apiService.getTopArtists("Bearer $token")
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error get Spotify api")
        }
    }

    suspend fun getTopTracks(token: String): Result<SpotifyTopTracksResponse> {
        return try {
            val response =  apiService.getTopTracks("Bearer $token")
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error get Spotify api")
        }
    }

    suspend fun getTrackUrl(token: String, query: String): String? {
        return try {
            val response = apiService.searchTracks("Bearer $token", query)
            response.tracks.items.firstOrNull()?.externalUrls?.spotify
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}