package com.example.melodate.ui.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.melodate.data.di.Injection

class MelodateViewModelFactory(
    private val appContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authTokenPreference = Injection.provideAuthTokenPreference(appContext)
        val matchRepository = Injection.provideMatchRepository()
        if (modelClass.isAssignableFrom(MelodateViewModel::class.java)) {
            return MelodateViewModel(matchRepository, authTokenPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

