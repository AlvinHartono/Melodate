package com.example.melodate.ui.matches

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melodate.data.model.Match
import com.example.melodate.databinding.ItemMatchedRowBinding
import com.example.melodate.ui.profile.profile.UserProfileActivity
import com.example.melodate.ui.profile.profile.UserProfileActivity.Companion.MATCHED_USER_ID

class MatchesListAdapter(val context: Context) :
    ListAdapter<Match, MatchesListAdapter.MatchesViewHolder>(DIFF_CALLBACK) {
    class MatchesViewHolder(val binding: ItemMatchedRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(match: Match) {
            binding.profileName.text = match.name
//            match user id = match.id
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
        holder.binding.profileImage.setOnClickListener {
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtra(MATCHED_USER_ID, match.id.toString())

            context.startActivity(intent)
        }
        holder.bind(match)
    }

}


