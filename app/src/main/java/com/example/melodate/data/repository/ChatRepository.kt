package com.example.melodate.data.repository

import com.example.melodate.data.Result
import com.example.melodate.data.remote.response.SendImageChatResponse
import com.example.melodate.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ChatRepository(private val apiService: ApiService) {

    suspend fun sendImage(
        roomId: RequestBody,
        sender: RequestBody,
        image: MultipartBody.Part
    ): Result<SendImageChatResponse> {
        return try {
            val response = apiService.sendImageChat(roomId, sender, image)

            if (response.file?.imageLink!!.isNotEmpty()) {
                Result.Success(response)
            } else {
                Result.Error("Error: an unkown error occured")
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

}