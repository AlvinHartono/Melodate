package com.example.melodate.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melodate.data.remote.response.User
import com.example.melodate.data.socketio.SocketManager
import com.example.melodate.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel =
            ViewModelProvider(this)[ChatViewModel::class.java]

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val chatListAdapter = ChatListAdapter()
        binding.rvChats.layoutManager = LinearLayoutManager(context)
        binding.rvChats.adapter = chatListAdapter

        val matchListAdapter = MatchesListAdapter()
        binding.rvMatches.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMatches.adapter = matchListAdapter

        // Create dummy users and submit the list to the adapter
        val dummyUsers = listOf(
            User(firstName = "Alice")
        )

        chatListAdapter.submitList(dummyUsers)
        matchListAdapter.submitList(dummyUsers)

        return root
    }

//    private fun listenForNewMessages() {
//        SocketManager.off()
//    }

    private fun initializeSocket(serverUrl: String) {
        Log.d("SocketManager", "Initializing socket...")
        if (!SocketManager.isInitialized()) {
            SocketManager.initialize(serverUrl)
            Log.d("SocketManager", "Socket initialized")
            SocketManager.connect()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}