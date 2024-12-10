package com.example.melodate.data.di

import android.content.Context
import com.example.melodate.data.local.database.AppDatabase
import com.example.melodate.data.preference.AuthTokenPreference
import com.example.melodate.data.preference.dataStore
import com.example.melodate.data.remote.retrofit.ApiConfig
import com.example.melodate.data.repository.AuthRepository
import com.example.melodate.data.repository.ChatRepository
import com.example.melodate.data.repository.MatchRepository

object Injection {
    fun provideAuthTokenPreference(context: Context): AuthTokenPreference {
        val dataStore = context.dataStore
        return AuthTokenPreference.getInstance(dataStore)
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val database = AppDatabase.getDatabase(context)
        return AuthRepository.getInstance(apiService, database.userDao())
    }

    fun provideMatchRepository(): MatchRepository {
        val apiService = ApiConfig.getMatchCardApiService()
        return MatchRepository(apiService)
    }

    fun provideChatRepository() : ChatRepository {
        val apiService = ApiConfig.getChatApiService()
        return ChatRepository(apiService)

    }
}