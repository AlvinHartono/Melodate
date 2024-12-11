package com.example.melodate.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melodate.data.model.Match
import com.example.melodate.databinding.ItemMatchedRowBinding

class MatchesListAdapter : ListAdapter<Match, MatchesListAdapter.MatchesViewHolder>(DIFF_CALLBACK) {
    class MatchesViewHolder(val binding: ItemMatchedRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(match: Match) {
            binding.profileName.text = match.name
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
    ): MatchesListAdapter.MatchesViewHolder {
        val binding =
            ItemMatchedRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchesListAdapter.MatchesViewHolder, position: Int) {
        val match = getItem(position)
        holder.bind(match)
    }
}