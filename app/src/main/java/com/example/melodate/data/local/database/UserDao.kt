package com.example.melodate.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userEntity: UserEntity)

    @Update
    suspend fun update(userEntity: UserEntity)

    @Query("UPDATE user SET name= :name, dob= :dob, gender= :gender, religion= :religion, education= :education, status= :status WHERE id = :id")
    suspend fun updatePersonalData(
        name: String,
        id: Int = 1,
        dob: String,
        gender: String,
        religion: String,
        education: String,
        status: String
    )

    //update general interest
    @Query("UPDATE user SET hobby= :hobby, height= :height, isSmoker= :isSmoker, isDrinker= :isDrinker, mbti= :mbti, loveLang= :loveLang WHERE id = :id")
    suspend fun updateGeneralInterest(
        hobby: String,
        id: Int = 1,
        height: Int,
        isSmoker: String,
        isDrinker: String,
        mbti: String,
        loveLang: String
    )

    //update user music preference
    @Query("UPDATE user SET genre= :genre, musicDecade= :musicDecade, musicVibe= :musicVibe, listeningFrequency= :listeningFrequency, concert= :concert WHERE id = :id")
    suspend fun updateMusicPreference(
        genre: String,
        id: Int = 1,
        musicDecade: String,
        musicVibe: String,
        listeningFrequency: String,
        concert: String
    )


    @Query("SELECT * FROM user WHERE id = 1")
    fun getUserData(): LiveData<UserEntity>
}