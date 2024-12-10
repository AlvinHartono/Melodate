package com.example.melodate.data.preference

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melodate.R
import com.example.melodate.data.remote.response.SpotifyTrack

class TopTracksAdapter : ListAdapter<SpotifyTrack, TopTracksAdapter.TrackViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.song_title)
        private val artistTextView: TextView = itemView.findViewById(R.id.song_artist)
        private val albumImageView: ImageView = itemView.findViewById(R.id.song_image)

        fun bind(track: SpotifyTrack) {
            titleTextView.text = track.name
            artistTextView.text = track.artists.joinToString(", ") { it.name } // Gabungkan nama artis
            Glide.with(itemView.context)
                .load(track.album.images.firstOrNull()?.url) // Ambil gambar pertama dari album
                .placeholder(R.drawable.apt) // Placeholder jika gambar kosong
                .into(albumImageView)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SpotifyTrack>() {
            override fun areItemsTheSame(oldItem: SpotifyTrack, newItem: SpotifyTrack): Boolean {
                // Gunakan kombinasi nama lagu dan nama album sebagai identifikasi unik sementara
                return (oldItem.name + oldItem.album.name) == (newItem.name + newItem.album.name)
            }

            override fun areContentsTheSame(oldItem: SpotifyTrack, newItem: SpotifyTrack): Boolean {
                return oldItem == newItem
            }
        }
    }
}


