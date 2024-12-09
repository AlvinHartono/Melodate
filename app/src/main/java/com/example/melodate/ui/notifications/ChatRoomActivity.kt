package com.example.melodate.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melodate.BuildConfig
import com.example.melodate.data.model.Message
import com.example.melodate.data.socketio.SocketManager
import com.example.melodate.databinding.ActivityChatRoomBinding
import org.json.JSONArray
import org.json.JSONObject

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding
    private val messageList = ArrayList<Message>()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serverUrl = BuildConfig.CHAT_URL


        val roomId = intent.getStringExtra("roomId") ?: ""
        val dummyRoom = "secret_room"
        val userName = intent.getStringExtra("userName") ?: "User"
        val dummyUserName = "U1"


        Log.d("ChatRoomActivity", "Room ID: $dummyRoom")
        initializeSocket(serverUrl)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = userName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()

        // Join the chat room
        Log.d("ChatRoomActivity", "Joining room: $dummyRoom")
        joinChatRoom(dummyRoom)

        binding.fabSend.setOnClickListener {
            val message = binding.editTextChat.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(roomId =  dummyRoom, sender =  dummyUserName, message =  message)
                binding.editTextChat.text.clear()
            }
        }

        // Listen for incoming messages
        listenForMessages()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Disconnect the socket and remove listeners when the activity is destroyed
        SocketManager.off("receive_message")
        SocketManager.disconnect()
    }

    private fun initializeSocket(serverUrl: String) {
        Log.d("SocketManager", "Initializing socket...")
        if (!SocketManager.isInitialized()) {
            SocketManager.initialize(serverUrl)
            Log.d("SocketManager", "Socket initialized")
            SocketManager.connect()
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(this, messageList)
        binding.recyclerViewChat.adapter = messageAdapter
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
    }

    private fun joinChatRoom(roomId: String) {

        Log.d("ChatRoomActivity", "Joining room: $roomId")
        SocketManager.joinChatRoom("join_room", roomId)
        Log.d("ChatRoomActivity", "Joined room: $roomId")
        Toast.makeText(this, "Joined room: $roomId", Toast.LENGTH_SHORT).show()

        //listen for previous messages when joining the room
        SocketManager.on("load_previous_messages") { args ->
            if (args.isNotEmpty()) {
                val previousMessages = args[0]
                runOnUiThread {
                    // Clear existing messages
                    messageList.clear()

                    // Handle different possible message format types
                    when (previousMessages) {
                        is JSONArray -> {
                            // If it's a JSONArray, convert it to a list of messages
                            for (i in 0 until previousMessages.length()) {
                                val messageData = previousMessages.getJSONObject(i)
                                val sender = messageData.optString("sender", "Unknown")
                                val message = messageData.optString("message", "")
                                messageList.add(Message(message, sender))
                            }
                        }
                        is List<*> -> {
                            // If it's already a List, process it as before
                            (previousMessages as List<Map<String, Any>>).forEach { messageData ->
                                val sender = messageData["sender"] as? String ?: "Unknown"
                                val message = messageData["message"] as? String ?: ""
                                messageList.add(Message(message, sender))
                            }
                        }
                        else -> {
                            // Log an error if the format is unexpected
                            Log.e("ChatRoomActivity", "Unexpected type for previous messages: ${previousMessages.javaClass}")
                        }
                    }

                    messageAdapter.notifyDataSetChanged()
                    if (messageList.isNotEmpty()) {
                        binding.recyclerViewChat.scrollToPosition(messageList.size - 1)
                    }
                }
            }
        }
    }

    private fun sendMessage(roomId: String, sender: String, message: String) {
        val messageData = JSONObject().apply {
            put("roomId", roomId)
            put("sender", sender)
            put("message", message)
        }
        Log.d("ChatRoomActivity", "Sending message: $messageData")
        SocketManager.emit("send_message", messageData)

        // Add message to the local list and update UI
        binding.recyclerViewChat.scrollToPosition(messageList.size - 1)
    }

    private fun listenForMessages() {

        SocketManager.on("receive_message") { args ->
            if (args.isNotEmpty()) {
                val data = args[0] as JSONObject
                val sender = data.optString("sender", "Unknown")
                val message = data.optString("message", "")

                runOnUiThread {
                    messageList.add(Message(message, sender))
                    messageAdapter.notifyItemInserted(messageList.size - 1)
                    binding.recyclerViewChat.scrollToPosition(messageList.size - 1)
                }
            }
        }
    }
}
