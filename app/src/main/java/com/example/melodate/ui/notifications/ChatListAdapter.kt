package com.example.melodate.ui.notifications

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melodate.data.remote.response.User
import com.example.melodate.databinding.ItemChatRowBinding

class ChatListAdapter : ListAdapter<User, ChatListAdapter.ChatViewHolder>(DIFF_CALLBACK) {
    class ChatViewHolder(val binding: ItemChatRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userName.text = user.firstName
            binding.latestChatMessage.text = ""

            //load picture
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatListAdapter.ChatViewHolder {
        val binding = ItemChatRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListAdapter.ChatViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        // intent to chat activity
        holder.itemView.setOnClickListener {
            // Handle item click here
            // You can start a new activity or perform any other action
            // For example:
             val intent = Intent(holder.itemView.context, ChatRoomActivity::class.java)
            intent.putExtra("userName", currentList[position].firstName)
            intent.putExtra("userId", currentList[position].user)
             holder.itemView.context.startActivity(intent)

        }

        // Set click listener for the item
    }
}