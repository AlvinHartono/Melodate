package com.example.melodate.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// datastore preference save token auth token
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "application")

interface IAuthTokenPreference {
    fun getAuthToken(): Flow<String?>
    suspend fun saveAuthToken(token: String)
    suspend fun deleteAuthToken()
}

class AuthTokenPreference private constructor(private val dataStore: DataStore<Preferences>): IAuthTokenPreference {

    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

    //GET AUTH TOKEN
    override fun getAuthToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[AUTH_TOKEN_KEY]
        }
    }

    //SAVE AUTH TOKEN
    override suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    //DELETE AUTH TOKEN
    override suspend fun deleteAuthToken() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }

    //singleton
    companion object {
        @Volatile
        private var INSTANCE: AuthTokenPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): AuthTokenPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthTokenPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}