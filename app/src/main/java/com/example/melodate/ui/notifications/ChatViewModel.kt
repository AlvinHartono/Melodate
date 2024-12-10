package com.example.melodate.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.melodate.data.Result
import com.example.melodate.data.remote.response.SendImageChatResponse
import com.example.melodate.data.repository.ChatRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _sendImageState = MutableLiveData<Result<SendImageChatResponse>>()
    val sendImageState: LiveData<Result<SendImageChatResponse>> = _sendImageState

    suspend fun sendImage(
        roomId: RequestBody,
        sender: RequestBody,
        image: MultipartBody.Part
    ): Result<SendImageChatResponse> {
        _sendImageState.postValue(Result.Loading)
        val response = chatRepository.sendImage(roomId, sender, image)
        _sendImageState.postValue(response)
        return response

    }

}