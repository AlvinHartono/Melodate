package com.example.melodate.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.melodate.R

class FavoriteArtistAdapter(
    private val songList: List<FavoriteArtistData>
) : RecyclerView.Adapter<FavoriteArtistAdapter.FavoriteSongViewHolder>() {

    inner class FavoriteSongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songImage: ImageView = view.findViewById(R.id.artist_image)
        val songArtist: TextView = view.findViewById(R.id.artist_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteSongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)
        return FavoriteSongViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteSongViewHolder, position: Int) {
        val song = songList[position]
        holder.songImage.setImageResource(song.songImage)
        holder.songArtist.text = song.artistName
    }

    override fun getItemCount(): Int = songList.size
}
