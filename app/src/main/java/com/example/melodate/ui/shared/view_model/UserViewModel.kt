package com.example.melodate.ui.shared.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melodate.data.Result
import com.example.melodate.data.remote.response.EditProfileResponse
import com.example.melodate.data.remote.response.GetUserDataResponse
import com.example.melodate.data.remote.response.MatchesListResponse
import com.example.melodate.data.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class UserViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    val userData = userRepository.getUserDataFromLocalDatabase()

    //live data
    private val _fetchUserDataState = MutableLiveData<Result<GetUserDataResponse>>()
    val fetchUserDataState: MutableLiveData<Result<GetUserDataResponse>> = _fetchUserDataState

    private val _editProfileState = MutableLiveData<Result<EditProfileResponse>>()
    val editProfileState: MutableLiveData<Result<EditProfileResponse>> = _editProfileState

    private val _updateUserLocalDatabaseState = MutableLiveData<Result<String>>()
    val updateUserLocalDatabaseState: MutableLiveData<Result<String>> =
        _updateUserLocalDatabaseState

    private val _fetchUserMatchesState = MutableLiveData<Result<MatchesListResponse>>()
    val fetchUserMatchesState: MutableLiveData<Result<MatchesListResponse>> = _fetchUserMatchesState

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> = _navigateToLogin

    init {
        updateUserLocalDatabase()
    }

    fun onLogoutRequested() {
        _navigateToLogin.postValue(true)
    }


    fun fetchUserData(userId: String): LiveData<Result<GetUserDataResponse>> {
        val userDataState = MutableLiveData<Result<GetUserDataResponse>>()
        userDataState.postValue(Result.Loading)

        viewModelScope.launch {
            val result = userRepository.fetchUserData(userId)
            userDataState.postValue(result)
        }

        return userDataState
    }

    private fun updateUserLocalDatabase() {
        _updateUserLocalDatabaseState.postValue(Result.Loading)
        viewModelScope.launch {
            val isSuccess = userRepository.updateUserLocalDatabase()
            _updateUserLocalDatabaseState.postValue(Result.Success(isSuccess))
        }
    }

    fun editProfile(
        status: RequestBody,
        education: RequestBody,
        religion: RequestBody,
        hobby: RequestBody,
        height: RequestBody,
        smoking: RequestBody,
        drinking: RequestBody,
        mbti: RequestBody,
        loveLanguage: RequestBody,
        genre: RequestBody,
        musicDecade: RequestBody,
        musicVibe: RequestBody,
        listeningFrequency: RequestBody,
        concert: RequestBody,
        location: RequestBody,
        bio: RequestBody,
//        profilePicture1: MultipartBody.Part? = null,
//        profilePicture2: MultipartBody.Part? = null,
//        profilePicture3: MultipartBody.Part? = null,
//        profilePicture4: MultipartBody.Part? = null,
//        profilePicture5: MultipartBody.Part? = null,
//        profilePicture6: MultipartBody.Part? = null
    ) {
        _editProfileState.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val response = userRepository.editProfileUserData(
                    status = status,
                    education = education,
                    religion = religion,
                    hobby = hobby,
                    height = height,
                    smoking = smoking,
                    drinking = drinking,
                    mbti = mbti,
                    loveLanguage = loveLanguage,
                    genre = genre,
                    musicDecade = musicDecade,
                    musicVibe = musicVibe,
                    listeningFrequency = listeningFrequency,
                    concert = concert,
                    location = location,
                    bio = bio,
//                    profilePicture1 = profilePicture1,
//                    profilePicture2 = profilePicture2,
//                    profilePicture3 = profilePicture3,
//                    profilePicture4 = profilePicture4,
//                    profilePicture5 = profilePicture5
//                    profilePicture6 = profilePicture6
                )

                when (response) {
                    is Result.Error -> _editProfileState.postValue(Result.Error(response.error))
                    Result.Loading -> _editProfileState.postValue(Result.Loading)
                    is Result.Success -> {
                        _editProfileState.postValue(Result.Success(response.data))
                    }
                }
            } catch (e: Exception) {
                _editProfileState.postValue(Result.Error(e.message.toString()))
            }
        }
    }

    suspend fun getCurrentUserIdFromPreference(): Int {
        return userRepository.getCurrentUserIdFromPreference() ?: 0
    }

    fun getUserMatches() {
        viewModelScope.launch {
            _fetchUserMatchesState.postValue(Result.Loading)
            when (val response = userRepository.getUserMatches()) {
                is Result.Error -> {
                    _fetchUserMatchesState.postValue(Result.Error(response.error))
                    Log.d("UserViewModel", "Error: ${response.error}")
                }

                Result.Loading -> {
                    _fetchUserMatchesState.postValue(Result.Loading)
                }

                is Result.Success -> {
                    _fetchUserMatchesState.postValue(Result.Success(response.data))
                }
            }
        }
    }

    fun likeUser(user1: Int, user2: Int) {
        viewModelScope.launch {
            _fetchUserMatchesState.postValue(Result.Loading)
            userRepository.likeUser(user1 = user1, user2 = user2)
        }
    }

}


