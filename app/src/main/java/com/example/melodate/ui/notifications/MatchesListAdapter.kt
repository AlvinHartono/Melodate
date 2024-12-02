package com.example.melodate.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melodate.data.remote.response.User
import com.example.melodate.databinding.ItemMatchedRowBinding

class MatchesListAdapter: ListAdapter<User, MatchesListAdapter.MatchesViewHolder>(DIFF_CALLBACK) {
    class MatchesViewHolder(val binding: ItemMatchedRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.profileName.text = user.firstName
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
    ): MatchesListAdapter.MatchesViewHolder {
        val binding = ItemMatchedRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchesListAdapter.MatchesViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
}