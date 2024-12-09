package com.example.melodate.data.repository

class ChatRepository {
//    private var socket: Socket = SocketManager.getSocket()
//
//    fun joinRoom(roomId: String) {
//        socket.emit("join_room", roomId)
//    }
//
//    fun sendMessage(roomId: String, message: String, sender: String) {
//        val messageData = JSONObject().apply {
//            put("roomId", roomId)
//            put("sender", sender)
//            put("message", message)
//        }
//        socket.emit("send_message", messageData)
//    }
//
//    fun listenForMessages(callback: (sender: String, message: String) -> Unit) {
//        socket.on("receive_message") { args ->
//            if (args.isNotEmpty()) {
//                val data = args[0] as JSONObject
//                val sender = data.getString("sender")
//                val message = data.getString("message")
//                callback(sender, message)
//            }
//        }
//    }
//
//    fun disconnect() {
//        socket.disconnect()
//    }

}