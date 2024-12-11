package com.example.melodate.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class SpotifyPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val SPOTIFY_TOKEN_KEY = stringPreferencesKey("spotify_token")
    private val SPOTIFY_REFRESH_TOKEN_KEY = stringPreferencesKey("spotify_refresh_token")
    private val KEY_TOKEN_EXPIRY = longPreferencesKey("spotify_token_expiry")

    suspend fun saveTokenData(token: String, refreshToken: String?, expiryTime: Long) {
        dataStore.edit { preferences ->
            preferences[SPOTIFY_TOKEN_KEY] = token
            if (refreshToken != null) {
                preferences[SPOTIFY_REFRESH_TOKEN_KEY] = refreshToken
            }
            preferences[KEY_TOKEN_EXPIRY] = expiryTime
        }
    }

    fun getTokenData(): Flow<TokenData?> = dataStore.data
        .map { preferences ->
            val token = preferences[SPOTIFY_TOKEN_KEY]
            val refreshToken = preferences[SPOTIFY_REFRESH_TOKEN_KEY]
            val expiryTime = preferences[KEY_TOKEN_EXPIRY]
            if (token != null && expiryTime != null) {
                TokenData(token, refreshToken, expiryTime)
            } else {
                null
            }
        }

    suspend fun clearTokenData() {
        dataStore.edit { preferences ->
            preferences.remove(SPOTIFY_TOKEN_KEY)
            preferences.remove(SPOTIFY_REFRESH_TOKEN_KEY)
            preferences.remove(KEY_TOKEN_EXPIRY)
        }
    }

    data class TokenData(
        val accessToken: String,
        val refreshToken: String?,
        val expiryTime: Long
    )

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


