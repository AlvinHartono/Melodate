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
import com.example.melodate.data.remote.response.SpotifyArtist

class TopArtistsAdapter : ListAdapter<SpotifyArtist, TopArtistsAdapter.ArtistViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)
        return ArtistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position)
        holder.bind(artist)
    }

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.artist_name)
        private val imageView: ImageView = itemView.findViewById(R.id.artist_image)

        fun bind(artist: SpotifyArtist) {
            nameTextView.text = artist.name

            // Ambil URL gambar pertama jika ada
            val imageUrl = artist.images.firstOrNull()?.url

            // Tampilkan gambar menggunakan Glide
            Glide.with(itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.newjeans) // Placeholder jika gambar kosong
                .into(imageView)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SpotifyArtist>() {
            override fun areItemsTheSame(oldItem: SpotifyArtist, newItem: SpotifyArtist): Boolean {
                // Gunakan properti URL Spotify sebagai pengganti ID
                return oldItem.externalUrls.spotify == newItem.externalUrls.spotify
            }

            override fun areContentsTheSame(oldItem: SpotifyArtist, newItem: SpotifyArtist): Boolean {
                return oldItem == newItem
            }
        }
    }
}


