package com.example.melodate.data.local.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "user")
@Parcelize
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 1,

    //email
    @ColumnInfo(name = "email")
    val email: String? = null,

    //password
    @ColumnInfo(name = "password")
    val password: String? = null,

    //name
    @ColumnInfo(name = "name")
    val name: String? = null,

    //hobby
    @ColumnInfo(name = "hobby")
    val hobby: String? = null,

    //age
    @ColumnInfo(name = "age")
    val age: Int? = null,

    //dob
    @ColumnInfo(name = "dob")
    val dob: String? = null,

    //status
    @ColumnInfo(name = "status")
    val status: String? = null,

    //gender
    @ColumnInfo(name = "gender")
    val gender: String? = null,

    //religion
    @ColumnInfo(name = "religion")
    val religion: String? = null,

    //education
    @ColumnInfo(name = "education")
    val education: String? = null,

    //height
    @ColumnInfo(name = "height")
    val height: Int? = null,

    //isSmoker
    @ColumnInfo(name = "isSmoker")
    val isSmoker: Boolean? = null,

    //isDrinker
    @ColumnInfo(name = "isDrinker")
    val isDrinker: Boolean? = null,

    //genre
    @ColumnInfo(name = "genre")
    val genre: String? = null,

    //musicDecade
    @ColumnInfo(name = "musicDecade")
    val musicDecade: String? = null,

    //concert
    @ColumnInfo(name = "concert")
    val concert: String? = null,

    //musicVibe
    @ColumnInfo(name = "musicVibe")
    val musicVibe: String? = null,

    //listening frequency
    @ColumnInfo(name = "listeningFrequency")
    val listeningFrequency: String? = null,

    //loveLang
    @ColumnInfo(name = "loveLang")
    val loveLang: String? = null,

    //mbti
    @ColumnInfo(name = "mbti")
    val mbti: String? = null,

    //bio
    @ColumnInfo(name = "bio")
    val bio: String? = null,

    //location
    @ColumnInfo(name = "location")
    val location: String? = null,

    //picture 1
    @ColumnInfo(name = "picture1")
    val picture1: String? = null,

    //picture 2
    @ColumnInfo(name = "picture2")
    val picture2: String? = null,

    //picture 3
    @ColumnInfo(name = "picture3")
    val picture3: String? = null,

    //picture 4
    @ColumnInfo(name = "picture4")
    val picture4: String? = null,

    //picture 5
    @ColumnInfo(name = "picture5")
    val picture5: String? = null,

    //picture 6
    @ColumnInfo(name = "picture6")
    val picture6: String? = null,


) : Parcelable