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
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class MelodateViewModel(
    private val repository: MatchRepository,
    private val authTokenPreference: AuthTokenPreference
) : ViewModel() {

    private val _matchCards = MutableLiveData<Result<List<MatchCard>>>()
    val matchCards: LiveData<Result<List<MatchCard>>> = _matchCards

    fun fetchRecommendations() {
        viewModelScope.launch {
            _matchCards.value = Result.Loading

            try {
                val userId = authTokenPreference.getUserId().firstOrNull()
                if (userId.isNullOrEmpty()) {
                    _matchCards.value = Result.Error("User ID not found")
                    return@launch
                }

                val response = repository.getRecommendations(userId)

                // Check if the response is successful
                if (response is Result.Success) {
                    _matchCards.value = response
                } else if (response is Result.Error) {
                    // Handle API error (e.g., specific messages from server)
                    val errorMessage = response.error ?: "Unknown server error"
                    _matchCards.value = Result.Error(errorMessage)
                }

            } catch (e: IOException) {
                // Handle network issues
                _matchCards.value = Result.Error("Network error. Please check your connection.")
            } catch (e: HttpException) {
                // Handle server-related issues
                val errorMessage = e.response()?.errorBody()?.string()?.let {
                    JSONObject(it).optString("message", "Unknown server error")
                } ?: "Unknown server error"
                _matchCards.value = Result.Error(errorMessage)
            } catch (e: Exception) {
                // Catch any other exceptions
                _matchCards.value = Result.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

}
