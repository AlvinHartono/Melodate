package com.example.melodate.ui.shared.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melodate.data.Result
import com.example.melodate.data.local.database.UserEntity
import com.example.melodate.data.model.User
import com.example.melodate.data.preference.AuthTokenPreference
import com.example.melodate.data.remote.response.LoginResponse
import com.example.melodate.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val authTokenPreference: AuthTokenPreference
) : ViewModel() {

    val authToken = authTokenPreference.getAuthToken()

    val userData = authRepository.userDataFromDatabase

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _confirmPassword = MutableLiveData("")
    val confirmPassword: LiveData<String> = _confirmPassword

    //view model for user
    private var _user = MutableLiveData<User>()
    var user: LiveData<User> = _user

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    // date of birth
    private val _dob = MutableLiveData("")
    val dob: LiveData<String> = _dob

    // gender
    private val _gender = MutableLiveData("")
    val gender: LiveData<String> = _gender

    // relationship status
    private val _relationshipStatus = MutableLiveData("")
    val relationshipStatus: LiveData<String> = _relationshipStatus

    //religion
    private val _religion = MutableLiveData("")
    val religion: LiveData<String> = _religion

    // education
    private val _education = MutableLiveData("")
    val education: LiveData<String> = _education

    //love language
    private val _loveLang = MutableLiveData("")
    val loveLang: LiveData<String> = _loveLang

    //hobby
    private val _hobby = MutableLiveData("")
    val hobby: LiveData<String> = _hobby

    //MBTI
    private val _mbti = MutableLiveData("")
    val mbti: LiveData<String> = _mbti

    //bio
    private val _bio = MutableLiveData("")
    val bio: LiveData<String> = _bio

    // height
    private val _height = MutableLiveData("")
    val height: LiveData<String> = _height

    // isSmoker
    private val _isSmoker = MutableLiveData("")
    val isSmoker: LiveData<String> = _isSmoker

    // isDrinker
    private val _isDrinker = MutableLiveData("")
    val isDrinker: LiveData<String> = _isDrinker

    //genre
    private val _genre = MutableLiveData("")
    val genre: LiveData<String> = _genre

    //music decade
    private val _musicDecade = MutableLiveData("")
    val musicDecade: LiveData<String> = _musicDecade

    //listening frequency
    private val _listeningFreq = MutableLiveData("")
    val listeningFreq: LiveData<String> = _listeningFreq

    //concert
    private val _concert = MutableLiveData("")
    val concert: LiveData<String> = _concert

    //music decade
    private val _musicVibe = MutableLiveData("")
    val musicVibe: LiveData<String> = _musicVibe


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginState = MutableLiveData<Result<LoginResponse>>()
    val loginState: LiveData<Result<LoginResponse>> = _loginState

    // user images
    private val _selectedImages = MutableLiveData<List<Uri>>(mutableListOf())
    val selectedImages: LiveData<List<Uri>> = _selectedImages

    fun addOrReplaceImage(position: Int, uri: Uri) {
        val currentList = _selectedImages.value?.toMutableList() ?: mutableListOf()
        if (currentList.size > position) {
            currentList[position] = uri
        } else {
            currentList.add(uri)
        }
        _selectedImages.value = currentList
    }

    fun saveUserEntity(userEntity: UserEntity) {
        viewModelScope.launch {
            authRepository.insert(userEntity)
        }
    }

    fun updatePersonalData(
        name: String,
        dob: String,
        gender: String,
        religion: String,
        education: String,
        status: String,
        age: Int
    ) {
        viewModelScope.launch {
            authRepository.updatePersonalData(name, dob, gender, religion, education, status, age)
        }
    }

    fun updateGeneralInterestData(
        hobby: String,
        height: Int,
        isSmoker: String,
        isDrinker: String,
        mbti: String,
        loveLang: String,
    ) {
        viewModelScope.launch {
            authRepository.updateGeneralInterestData(
                hobby,
                height,
                isSmoker,
                isDrinker,
                mbti,
                loveLang
            )
        }
    }

    fun updateMusicPreferenceData(
        genre: String,
        musicDecade: String,
        musicVibe: String,
        listeningFrequency: String,
        concert: String,
    ) {
        viewModelScope.launch {
            authRepository.updateMusicPreferenceData(
                genre,
                musicDecade,
                musicVibe,
                listeningFrequency,
                concert
            )
        }
    }

    //functions for setting data
    fun setName(name: String) {
        _name.value = name
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    private fun saveAuthToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authTokenPreference.saveAuthToken(token)
        }
    }

    //set gender
    fun setGender(gender: String) {
        _gender.value = gender
    }

    fun setDob(dob: String) {
        _dob.value = dob
    }

    fun setRelationshipStatus(status: String) {
        _relationshipStatus.value = status
    }

    fun setReligion(religion: String) {
        _religion.value = religion
    }

    fun setEducation(education: String) {
        _education.value = education
    }

    fun setHobby(hobby: String) {
        _hobby.value = hobby
    }

    fun setLoveLang(loveLang: String) {
        _loveLang.value = loveLang
    }

    fun setMbti(mbti: String) {
        _mbti.value = mbti
    }

    fun setBio(bio: String) {
        _bio.value = bio
    }

    fun setHeight(height: String) {
        _height.value = height
    }

    fun setIsSmoker(isSmoker: Boolean) {
        _isSmoker.value = if (isSmoker) "Yes" else "No"
    }

    fun setIsDrinker(isDrinker: Boolean) {
        _isDrinker.value = if (isDrinker) "Yes" else "No"
    }

    fun setGenre(genre: String) {
        _genre.value = genre
    }

    fun setMusicDecade(musicDecade: String) {
        _musicDecade.value = musicDecade
    }

    fun setMusicVibe(musicVibe: String) {
        _musicVibe.value = musicVibe
    }

    fun setListeningFreq(listeningFreq: String) {
        _listeningFreq.value = listeningFreq
    }

    fun setConcert(concert: String) {
        _concert.value = concert
    }


    //check email result
    private val _isEmailAvailable = MutableLiveData<Result<Boolean>>()
    val isEmailAvailable: LiveData<Result<Boolean>> = _isEmailAvailable

    // check if email is available or not
    fun checkEmail(email: String) {
        viewModelScope.launch {
            _isEmailAvailable.postValue(Result.Loading)

            try {
                val response = authRepository.checkEmail(email)
                _isEmailAvailable.postValue(response)
            } catch (e: Exception) {
                _isEmailAvailable.postValue(Result.Error(e.message.toString()))
            }
        }
    }

    // log user in
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.postValue(Result.Loading)
            try {
                when (val response = authRepository.login(email, password)) {
                    is Result.Error -> {
                        _loginState.postValue(Result.Error(response.error))
                    }

                    Result.Loading ->
                        _loginState.postValue(Result.Loading)

                    is Result.Success -> {
                        _loginState.postValue(Result.Success(response.data))
                        saveAuthToken(response.data.user?.token.toString())
                    }
                }
            } catch (e: Exception) {
                _loginState.postValue(Result.Error(e.message.toString()))
            }
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            authTokenPreference.deleteAuthToken()
        }
    }

    companion object {
        val NAME = "name"
        val AGE = "age"
        val STATUS = "status"
        val GENDER = "gender"
        val RELIGION = "religion"
        val EDUCATION = "education"
        val HEIGHT = "height"
        val IS_SMOKER = "isSmoker"
        val IS_DRINKER = "isDrinker"
        val GENRE = "genre"
        val MUSIC_DECADE = "musicDecade"
        val LISTENING_FREQ = "listeningFreq"
        val CONCERT = "concert"
        val LOVE_LANG = "loveLang"
    }

}