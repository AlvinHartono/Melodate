package com.example.melodate.ui.notifications

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melodate.data.model.Message
import com.example.melodate.databinding.ActivityChatRoomBinding

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding
    private lateinit var messageList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = userName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        messageList = ArrayList()
        messageList.add(Message("Hello, how are you?", "U1"))
        messageList.add(Message("I'm good, thanks!", "U2"))
        messageList.add(Message("What about you?", "U1"))

        val messageAdapter = MessageAdapter(this, messageList)
        binding.recyclerViewChat.adapter = messageAdapter
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChat.scrollToPosition(messageAdapter.itemCount - 1)
        binding.fabSend.setOnClickListener {
            val message = binding.editTextChat.text.toString()
            if (message.isNotEmpty()) {
                messageList.add(Message(message, "U1"))
                messageAdapter.notifyItemInserted(messageAdapter.itemCount - 1)
                binding.recyclerViewChat.scrollToPosition(messageAdapter.itemCount - 1)
                binding.editTextChat.text.clear()
            }

        }
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

}