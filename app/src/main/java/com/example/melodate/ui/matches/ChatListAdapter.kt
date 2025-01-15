package com.example.melodate.ui.matches

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melodate.data.model.Match
import com.example.melodate.databinding.ItemChatRowBinding

class ChatListAdapter : ListAdapter<Match, ChatListAdapter.ChatViewHolder>(DIFF_CALLBACK) {
    class ChatViewHolder(val binding: ItemChatRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(match: Match) {
            binding.userName.text = match.name
            binding.latestChatMessage.text = ""
            //load picture
            Glide.with(binding.root.context).load(match.profileImg).circleCrop()
                .into(binding.profileImage)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Match>() {
            override fun areItemsTheSame(
                oldItem: Match,
                newItem: Match
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Match,
                newItem: Match
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
            intent.putExtra("userName", currentList[position].name)
            intent.putExtra("userId", currentList[position].id)
            intent.putExtra("currentUserId", currentList[position].currentUserId)
            intent.putExtra("roomId", currentList[position].roomId)
            intent.putExtra("profileImg", currentList[position].profileImg)
            holder.itemView.context.startActivity(intent)

        }

        // Set click listener for the item
    }
}