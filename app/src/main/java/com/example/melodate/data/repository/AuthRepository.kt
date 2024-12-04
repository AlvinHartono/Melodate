package com.example.melodate.data.repository

import androidx.lifecycle.LiveData
import com.example.melodate.data.Result
import com.example.melodate.data.local.database.UserDao
import com.example.melodate.data.local.database.UserEntity
import com.example.melodate.data.remote.request.CheckEmailRequest
import com.example.melodate.data.remote.request.LoginRequest
import com.example.melodate.data.remote.response.DeleteAccountResponse
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
        val isSmokerBool = isSmoker == "Yes"
        val isDrinkerBool = isDrinker == "Yes"

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
        age: RequestBody,
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
        musicVibe: RequestBody,
        listeningFrequency: RequestBody,
        concert: RequestBody,
        profilePicture1: MultipartBody.Part? = null,
        profilePicture2: MultipartBody.Part? = null,
        profilePicture3: MultipartBody.Part? = null,
        profilePicture4: MultipartBody.Part? = null,
        profilePicture5: MultipartBody.Part? = null,
        profilePicture6: MultipartBody.Part? = null
    ): Result<RegisterResponse> {
        return try {
            val result = Result.Success(
                apiService.registerUser(
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    firstName = firstName,
                    dateOfBirth = dateOfBirth,
                    age = age,
                    gender = gender,
                    relationshipStatus = relationshipStatus,
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
                    profilePicture1 = profilePicture1,
                    profilePicture2 = profilePicture2,
                    profilePicture3 = profilePicture3,
                    profilePicture4 = profilePicture4,
                    profilePicture5 = profilePicture5,
                    profilePicture6 = profilePicture6
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

    suspend fun deleteUser(requestBody: RequestBody): Result<DeleteAccountResponse> {
        try {
            val response = apiService.deleteUser(requestBody)
            return if (response.error!!) {
                Result.Error(response.message.toString())
            } else {
                Result.Success(response)
            }
        } catch (e: Exception) {
            return Result.Error(e.message.toString())
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