package com.example.melodate.data.preference

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DarkModeViewModel(private val preferences: SettingPreferences) : ViewModel() {

    val isDarkMode: LiveData<Boolean> = preferences.isDarkMode.asLiveData()

    fun setDarkMode(isEnabled: Boolean) {
        viewModelScope.launch {
            preferences.setDarkMode(isEnabled)
        }
    }

}

class DarkModeViewModelFactory(private val preferences: SettingPreferences) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DarkModeViewModel::class.java)) {
            return DarkModeViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
