package com.example.melodate.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melodate.data.remote.response.MatchCard
import com.example.melodate.data.repository.MatchRepository
import com.example.melodate.data.Result
import com.example.melodate.data.preference.AuthTokenPreference
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MelodateViewModel(
    private val repository: MatchRepository,
    private val authTokenPreference: AuthTokenPreference
) : ViewModel() {

    private val _matchCards = MutableLiveData<Result<List<MatchCard>>>()
    val matchCards: LiveData<Result<List<MatchCard>>> = _matchCards

    fun fetchRecommendations() {
        viewModelScope.launch {
            _matchCards.value = Result.Loading
            authTokenPreference.getUserId()
                .firstOrNull()
                ?.let { userId ->
                    _matchCards.value = repository.getRecommendations(userId)
                } ?: run {
                _matchCards.value = Result.Error("User ID not found")
            }
        }
    }
}
