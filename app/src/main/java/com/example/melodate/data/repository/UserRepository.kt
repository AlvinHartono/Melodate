package com.example.melodate.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.melodate.data.Result
import com.example.melodate.data.local.database.UserDao
import com.example.melodate.data.local.database.UserEntity
import com.example.melodate.data.preference.AuthTokenPreference
import com.example.melodate.data.remote.response.EditProfileResponse
import com.example.melodate.data.remote.response.GetUserDataResponse
import com.example.melodate.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.RequestBody

class UserRepository(
    private val userDao: UserDao,
    private val apiService: ApiService,
    private val authTokenPreference: AuthTokenPreference
) {

    fun getUserDataFromLocalDatabase(): LiveData<UserEntity> {
        return userDao.getUserData()
    }


    suspend fun fetchUserData(userId: String?): Result<GetUserDataResponse> {
        if (userId == null) {
            authTokenPreference.getUserId().firstOrNull().let { currentUserId ->
                val response = apiService.getUserData(currentUserId!!)
                return when (response.error) {
                    true -> Result.Error(response.message.toString())
                    false -> Result.Success(response)
                    null -> Result.Error("Unknown error")
                }
            }
        } else {
            val response = apiService.getUserData(userId)
            return when (response.error) {
                true -> Result.Error(response.message.toString())
                false -> Result.Success(response)
                null -> Result.Error("Unknown error")
            }
        }
    }

    private suspend fun updateUserDataToLocalDatabase(userData: GetUserDataResponse) {

        val userEntity = UserEntity(
            id = userData.user?.user ?: 1,
            email = userData.user?.email,
            password = "",
            name = userData.user?.firstName,
            hobby = userData.user?.hobby,
            age = userData.user?.age,
            dob = userData.user?.dateOfBirth,
            status = userData.user?.status,
            gender = userData.user?.gender,
            religion = userData.user?.religion,
            education = userData.user?.education,
            height = userData.user?.height?.toInt(),
            isSmoker = userData.user?.smokes == null,
            isDrinker = userData.user?.drinks == null,
            genre = userData.user?.genre,
            musicDecade = userData.user?.musicDecade,
            concert = userData.user?.concert,
            musicVibe = userData.user?.musicVibe,
            listeningFrequency = userData.user?.listeningFrequency,
            loveLang = userData.user?.loveLanguage,
            mbti = userData.user?.mbti,
            bio = userData.user?.biodata.toString(),
            location = userData.user?.location.toString(),
            picture1 = userData.user?.profilePicture1,
            picture2 = userData.user?.profilePicture2,
            picture3 = userData.user?.profilePicture3,
            picture4 = userData.user?.profilePicture4,
            picture5 = userData.user?.profilePicture5,
            picture6 = userData.user?.profilePicture6
        )
        userDao.deleteAll()
        userDao.insert(userEntity)
    }

    suspend fun updateUserLocalDatabase(): String {
        val userId = authTokenPreference.getUserId().firstOrNull()
        Log.d("UserRepository", "userId: $userId")
        val userInDatabase = userDao.getUserData()

        if (userInDatabase.value == null || userInDatabase.value?.id != userId?.toInt()) {
            when (val userData = fetchUserData(userId)) {
                is Result.Error -> return userData.error
                Result.Loading -> {}
                is Result.Success ->
                    updateUserDataToLocalDatabase(userData.data)
            }
        }
        return "Success ${userInDatabase.value?.id}"
    }

    suspend fun editProfileUserData(
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

    ): Result<EditProfileResponse> {
        val userId = authTokenPreference.getUserId().firstOrNull()
        Log.d("UserRepository", "userId: $userId")
        return try {
            val response =
                apiService.updateUserData(
                    userId = userId.toString(),
                    relationshipStatus = status,
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
//                    profilePicture5 = profilePicture5,
//                    profilePicture6 = profilePicture6
                )

            if (response.error) {
                Result.Error(response.message.toString())
            } else {
                Result.Success(response)
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }
}
