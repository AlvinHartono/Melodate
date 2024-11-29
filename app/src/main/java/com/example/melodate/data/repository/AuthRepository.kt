package com.example.melodate.data.repository

import androidx.lifecycle.LiveData
import com.example.melodate.data.Result
import com.example.melodate.data.local.database.UserDao
import com.example.melodate.data.local.database.UserEntity
import com.example.melodate.data.remote.request.CheckEmailRequest
import com.example.melodate.data.remote.request.LoginRequest
import com.example.melodate.data.remote.response.LoginResponse
import com.example.melodate.data.remote.retrofit.ApiService

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


//    suspend fun register(name: String, email: String, password: String): Result<LoginResponse> {
//
//    }

    companion object {
        //singleton
        private var instance: AuthRepository? = null
        fun getInstance(apiService: ApiService, userDao: UserDao): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userDao)
            }.also { instance = it }
    }
}