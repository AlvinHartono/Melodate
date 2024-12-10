package com.example.melodate.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.melodate.data.di.Injection
import com.example.melodate.data.repository.ChatRepository

class ChatViewModelFactory(
    private val chatRepository: ChatRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val chatRepository = Injection.provideChatRepository()
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(chatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        private var instance: ChatViewModelFactory? = null
        fun getInstance(): ChatViewModelFactory = instance ?: synchronized(this) {
            instance ?: ChatViewModelFactory(Injection.provideChatRepository())
        }.also { instance = it }

    }
}
