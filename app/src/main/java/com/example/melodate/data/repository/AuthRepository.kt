package com.example.melodate.data.repository

import androidx.lifecycle.LiveData
import com.example.melodate.data.Result
import com.example.melodate.data.local.database.UserDao
import com.example.melodate.data.local.database.UserEntity
import com.example.melodate.data.remote.request.CheckEmailRequest
import com.example.melodate.data.remote.request.LoginRequest
import com.example.melodate.data.remote.response.LoginResponse
import com.example.melodate.data.remote.response.RegisterResponse
import com.example.melodate.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthRepository(private val apiService: ApiService, private val userDao: UserDao) {


    suspend fun checkEmail(email: String): Result<Boolean> {
        return try {
            val checkEmailRequest = CheckEmailRequest(email)
            val response = apiService.checkEmail(checkEmailRequest)

            if (response.error == true) {
                Result.Error(response.message.toString())
            } else {
                Result.Success(response.available == true)
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val loginRequest = LoginRequest(email, password)
            val response = apiService.loginUser(loginRequest)

            if (response.error == true) {
                Result.Error(response.message.toString())
            } else {
                Result.Success(response)
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    val userDataFromDatabase: LiveData<UserEntity> = userDao.getUserData()

    suspend fun insert(userEntity: UserEntity) {
        userDao.insert(userEntity)
    }

    suspend fun updatePersonalData(
        name: String,
        dob: String,
        gender: String,
        religion: String,
        education: String,
        status: String,
        age: Int
    ) {
        userDao.updatePersonalData(
            name = name,
            dob = dob,
            gender = gender,
            religion = religion,
            education = education,
            status = status,
            age = age
        )
    }

    suspend fun updateGeneralInterestData(
        hobby: String,
        height: Int,
        isSmoker: String,
        isDrinker: String,
        mbti: String,
        loveLang: String,
    ) {
        //convert smoke and drink
        val isSmokerBool = if (isSmoker == "Yes") true else false
        val isDrinkerBool = if (isDrinker == "Yes") true else false

        userDao.updateGeneralInterest(
            hobby = hobby,
            height = height,
            isSmoker = isSmokerBool,
            isDrinker = isDrinkerBool,
            mbti = mbti,
            loveLang = loveLang,
        )
    }

    suspend fun updateMusicPreferenceData(
        genre: String,
        musicDecade: String,
        musicVibe: String,
        listeningFrequency: String,
        concert: String,
    ) {
        userDao.updateMusicPreference(
            genre = genre,
            musicDecade = musicDecade,
            musicVibe = musicVibe,
            listeningFrequency = listeningFrequency,
            concert = concert,
        )
    }


    suspend fun registerUser(
        email: RequestBody,
        password: RequestBody,
        confirmPassword: RequestBody,
        firstName: RequestBody,
        dateOfBirth: RequestBody,
        gender: RequestBody,
        relationshipStatus: RequestBody,
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
        listeningFrequency: RequestBody,
        concert: RequestBody,
        profilePicture1: MultipartBody.Part?,
        profilePicture2: MultipartBody.Part?,
        profilePicture3: MultipartBody.Part?,
        profilePicture4: MultipartBody.Part?,
        profilePicture5: MultipartBody.Part?,
        profilePicture6: MultipartBody.Part?
    ): Result<RegisterResponse> {
        return try {
            val result = Result.Success(
                apiService.registerUser(
                    email,
                    password,
                    confirmPassword,
                    firstName,
                    dateOfBirth,
                    gender,
                    relationshipStatus,
                    education,
                    religion,
                    hobby,
                    height,
                    smoking,
                    drinking,
                    mbti,
                    loveLanguage,
                    genre,
                    musicDecade,
                    listeningFrequency,
                    concert,
                    profilePicture1,
                    profilePicture2,
                    profilePicture3,
                    profilePicture4,
                    profilePicture5,
                    profilePicture6
                )
            )
            if (result.data.error == true) {
                Result.Error(result.data.message.toString())
            } else {
                Result.Success(result.data)
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }

    }

    companion object {
        //singleton
        private var instance: AuthRepository? = null
        fun getInstance(apiService: ApiService, userDao: UserDao): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userDao)
            }.also { instance = it }
    }
}