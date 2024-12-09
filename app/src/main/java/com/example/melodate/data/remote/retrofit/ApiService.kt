package com.example.melodate.data.remote.retrofit

import com.example.melodate.data.remote.request.CheckEmailRequest
import com.example.melodate.data.remote.request.LoginRequest
import com.example.melodate.data.remote.response.CheckEmailResponse
import com.example.melodate.data.remote.response.DeleteAccountResponse
import com.example.melodate.data.remote.response.LoginResponse
import com.example.melodate.data.remote.response.MatchCard
import com.example.melodate.data.remote.response.RegisterResponse
import com.example.melodate.data.remote.response.SpotifyTopArtistsResponse
import com.example.melodate.data.remote.response.SpotifyTopTracksResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("/api/users/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("/api/users/checkEmail")
    suspend fun checkEmail(
        @Body request: CheckEmailRequest
    ):CheckEmailResponse

    // DELETE request dengan body
    @HTTP(method = "DELETE", path = "/api/users/delete", hasBody = true)
    suspend fun deleteUser(
        @Body requestBody: RequestBody
    ): DeleteAccountResponse

    @Multipart
    @POST("/api/users/register")
    suspend fun registerUser(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirmPassword") confirmPassword: RequestBody,
        @Part("firstName") firstName: RequestBody,
        @Part("date_of_birth") dateOfBirth: RequestBody,
        @Part("age") age: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("status") relationshipStatus: RequestBody,
        @Part("education") education: RequestBody,
        @Part("religion") religion: RequestBody,
        @Part("hobby") hobby: RequestBody,
        @Part("height") height: RequestBody,
        @Part("smokes") smoking: RequestBody,
        @Part("drinks") drinking: RequestBody,
        @Part("mbti") mbti: RequestBody,
        @Part("love_language") loveLanguage: RequestBody,
        @Part("genre") genre: RequestBody,
        @Part("music_decade") musicDecade: RequestBody,
        @Part("music_vibe") musicVibe: RequestBody,
        @Part("listening_frequency") listeningFrequency: RequestBody,
        @Part("concert") concert: RequestBody,
        @Part profilePicture1: MultipartBody.Part? = null,
        @Part profilePicture2: MultipartBody.Part? = null,
        @Part profilePicture3: MultipartBody.Part? = null,
        @Part profilePicture4: MultipartBody.Part? = null,
        @Part profilePicture5: MultipartBody.Part? = null,
        @Part profilePicture6: MultipartBody.Part? = null
    ): RegisterResponse

    @GET("/recommendations")
    suspend fun getRecommendations(
        @Query("userid") userId: String,
        @Query("top_n") topN: Int = 5
    ): List<MatchCard>

    @GET("me/top/artists")
    suspend fun getTopArtists(
        @Header("Authorization") token: String,
        @Query("time_range") timeRange: String = "medium_term",
        @Query("limit") limit: Int = 5
    ): SpotifyTopArtistsResponse

    @GET("me/top/tracks")
    suspend fun getTopTracks(
        @Header("Authorization") token: String,
        @Query("time_range") timeRange: String = "medium_term",
        @Query("limit") limit: Int = 5
    ): SpotifyTopTracksResponse

}