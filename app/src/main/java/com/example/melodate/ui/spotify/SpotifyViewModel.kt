package com.example.melodate.ui.spotify

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melodate.data.preference.SpotifyPreference
import com.example.melodate.data.remote.response.SpotifyArtist
import com.example.melodate.data.remote.response.SpotifyTrack
import com.example.melodate.data.repository.SpotifyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.melodate.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class SpotifyViewModel(
    private val repository: SpotifyRepository,
    private val spotifyPreference: SpotifyPreference
) : ViewModel() {

    private val CLIENT_ID: String = "33f84566fd954b529f82d6bd0e42cdc1"
    private val CLIENT_SECRET: String = "b98c8977fd2548d0a12a73cc11f1a2d3"

    private val _topArtists = MutableLiveData<List<SpotifyArtist>>()
    val topArtists: LiveData<List<SpotifyArtist>> = _topArtists

    private val _topTracks = MutableLiveData<List<SpotifyTrack>>()
    val topTracks: LiveData<List<SpotifyTrack>> = _topTracks

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _currentQuery = MutableLiveData<String?>()
    val currentQuery: LiveData<String?> get() = _currentQuery

    fun setCurrentQuery(query: String?) {
        _currentQuery.value = query
    }

    fun fetchTrackUrl(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            getSpotifyTokenData().collect { tokenData ->
                if (tokenData != null && _currentQuery.value != null) {
                    val token = tokenData.accessToken
                    val trackUrl = repository.getTrackUrl(token, _currentQuery.value!!)
                    onResult(trackUrl)
                } else {
                    onResult(null)
                }
            }
        }
    }


    fun saveSpotifyTokenData(token: String, refreshToken: String?, expiryTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            spotifyPreference.saveTokenData(token, refreshToken, expiryTime)
        }
    }

    fun getSpotifyTokenData(): Flow<SpotifyPreference.TokenData?> = spotifyPreference.getTokenData()

    fun fetchTopArtists(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTopArtists(token)
                if (response is Result.Success) {
                    _topArtists.postValue(response.data.artists)
                } else if (response is Result.Error) {
                    _error.postValue(response.error)
                }
            } catch (e: Exception) {
                _error.postValue("Failed to fetch top artists")
            }
        }
    }

    fun fetchTopTracks(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTopTracks(token)
                if (response is Result.Success) {
                    _topTracks.postValue(response.data.tracks)
                } else if (response is Result.Error) {
                    _error.postValue(response.error)
                }
            } catch (e: Exception) {
                _error.postValue("Failed to fetch top tracks")
            }
        }
    }


    fun refreshSpotifyToken() {
        viewModelScope.launch {
            val tokenData = spotifyPreference.getTokenData().firstOrNull()
            val refreshToken = tokenData?.refreshToken

            if (refreshToken != null) {
                val body = "grant_type=refresh_token&refresh_token=$refreshToken"
                    .toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())

                val authHeader = "Basic " + Base64.encodeToString(
                    "$CLIENT_ID:$CLIENT_SECRET".toByteArray(),
                    Base64.NO_WRAP
                )

                try {
                    val response = withContext(Dispatchers.IO) {
                        val request = Request.Builder()
                            .url("https://accounts.spotify.com/api/token")
                            .post(body)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Authorization", authHeader)
                            .build()

                        OkHttpClient().newCall(request).execute()
                    }

                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        responseBody?.let {
                            val json = JSONObject(it)
                            val newAccessToken = json.getString("access_token")
                            val expiresIn = json.getLong("expires_in")

                            spotifyPreference.saveTokenData(
                                newAccessToken,
                                refreshToken,
                                System.currentTimeMillis() + expiresIn * 1000
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SpotifyTokenRefresh", "Error refreshing token: ${e.message}")
                }
            }
        }
    }
}


