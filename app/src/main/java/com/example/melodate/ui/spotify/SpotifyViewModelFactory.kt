package com.example.melodate.ui.spotify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.melodate.data.preference.SpotifyPreference
import com.example.melodate.data.repository.SpotifyRepository

@Suppress("UNCHECKED_CAST")
class SpotifyViewModelFactory(
    private val repository: SpotifyRepository,
    private val spotifyPreference: SpotifyPreference
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SpotifyViewModel(repository, spotifyPreference) as T
    }
}

