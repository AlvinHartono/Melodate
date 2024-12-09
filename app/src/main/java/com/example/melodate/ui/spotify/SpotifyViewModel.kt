package com.example.melodate.ui.spotify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melodate.data.preference.SpotifyPreference
import com.example.melodate.data.remote.response.SpotifyArtist
import com.example.melodate.data.remote.response.SpotifyTrack
import com.example.melodate.data.repository.SpotifyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.melodate.data.Result

class SpotifyViewModel(
    private val repository: SpotifyRepository,
    private val spotifyPreference: SpotifyPreference
) : ViewModel() {

    private val _topArtists = MutableLiveData<List<SpotifyArtist>>()
    val topArtists: LiveData<List<SpotifyArtist>> = _topArtists

    private val _topTracks = MutableLiveData<List<SpotifyTrack>>()
    val topTracks: LiveData<List<SpotifyTrack>> = _topTracks

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Save Spotify token
    fun saveSpotifyToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            spotifyPreference.saveSpotifyToken(token)
        }
    }

    // Fetch top artists
    fun fetchTopArtists() {
        viewModelScope.launch {
            val token = spotifyPreference.getSpotifyToken().first()
            token?.let {
                try {
                    val response = repository.getTopArtists(token)
                    if (response is Result.Success) {
                        _topArtists.postValue(response.data.artists)
                    }
                } catch (e: Exception) {
                    _error.postValue("Failed to fetch top artists")
                }
            }
        }
    }

    // Fetch top tracks
    fun fetchTopTracks() {
        viewModelScope.launch {
            val token = spotifyPreference.getSpotifyToken().first()
            token?.let {
                try {
                    val response = repository.getTopTracks(token)
                    if (response is Result.Success) {
                        _topTracks.postValue(response.data.tracks)
                    }
                } catch (e: Exception) {
                    _error.postValue("Failed to fetch top tracks")
                }
            }
        }
    }
}

