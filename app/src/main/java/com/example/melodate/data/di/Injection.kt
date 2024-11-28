package com.example.melodate.data.di

import android.content.Context
import com.example.melodate.data.local.database.AppDatabase
import com.example.melodate.data.preference.AuthTokenPreference
import com.example.melodate.data.preference.dataStore
import com.example.melodate.data.remote.retrofit.ApiConfig
import com.example.melodate.data.repository.AuthRepository

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
}