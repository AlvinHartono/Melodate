package com.example.melodate.data.socketio

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException

object SocketManager {
    private var socket: Socket? = null

    fun initialize(baseUrl: String) {
        try {
            socket = IO.socket(baseUrl)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun connect() {
        Log.d("SocketManager", "Connecting to socket...")
        socket?.connect()

        Log.d("SocketManager", "Socket connected: ${socket?.connected()}")

        socket?.on(Socket.EVENT_CONNECT) {
            Log.d("SocketManager", "Socket connected successfully")
        }

        socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e("SocketManager", "Socket connection error: ${args.joinToString()}")
        }
    }

    fun disconnect() {
        socket?.disconnect()
        socket = null
    }

    fun joinChatRoom(event: String, data: String){
        socket?.emit(event, data)
        Log.d("SocketManager", "Event emitted: $event")
    }

    fun emit(event: String, data: JSONObject) {
        Log.d("SocketManager", "Emitting event: $event with data: $data")
        socket?.emit(event, data)
        Log.d("SocketManager", "Event emitted: $event")
    }

    fun on(event: String, listener: (Array<Any>) -> Unit) {
        socket?.on(event) { args ->
            listener(args)
        }
    }

    fun off(event: String) {
        socket?.off(event)
    }

    fun isInitialized(): Boolean = socket != null
}