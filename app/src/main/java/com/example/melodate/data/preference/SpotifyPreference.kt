package com.example.melodate.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class SpotifyPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val SPOTIFY_TOKEN_KEY = stringPreferencesKey("spotify_token")

    suspend fun saveSpotifyToken(token: String) {
        dataStore.edit { preferences ->
            preferences[SPOTIFY_TOKEN_KEY] = token
        }
    }

    fun getSpotifyToken(): Flow<String?> = dataStore.data
        .map { preferences -> preferences[SPOTIFY_TOKEN_KEY] }

    suspend fun deleteSpotifyToken() {
        dataStore.edit { preferences ->
            preferences.remove(SPOTIFY_TOKEN_KEY)
        }
    }

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: SpotifyPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SpotifyPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SpotifyPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
