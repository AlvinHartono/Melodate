package com.example.melodate.data.model

data class Message(
    var message: String? = null,
    var senderId: Int? = null,
    var currentUserId: Int? = null
)