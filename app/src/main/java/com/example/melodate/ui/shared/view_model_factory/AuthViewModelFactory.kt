package com.example.melodate.ui.shared.view_model_factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.melodate.data.di.Injection
import com.example.melodate.data.repository.AuthRepository
import com.example.melodate.ui.shared.view_model.AuthViewModel

class AuthViewModelFactory private constructor(
    private val appContext: Context,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val authTokenPreference = Injection.provideAuthTokenPreference(appContext)
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository, authTokenPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        private var instance: AuthViewModelFactory? = null
        fun getInstance(context: Context): AuthViewModelFactory = instance ?: synchronized(this) {
            instance ?: AuthViewModelFactory(context, Injection.provideAuthRepository(context))
        }.also { instance = it }

    }
}