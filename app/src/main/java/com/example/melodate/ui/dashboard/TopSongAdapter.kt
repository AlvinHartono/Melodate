package com.example.melodate.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.melodate.R

class TopSongAdapter(
    private val artistList: List<TopSongData>
) : RecyclerView.Adapter<TopSongAdapter.TopArtistViewHolder>() {

    inner class TopArtistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val artistImage: ImageView = view.findViewById(R.id.song_image)
        val songTitle: TextView = view.findViewById(R.id.song_title)
        val artistName: TextView = view.findViewById(R.id.song_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return TopArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopArtistViewHolder, position: Int) {
        val artist = artistList[position]
        holder.artistImage.setImageResource(artist.artistImage)
        holder.songTitle.text = artist.songTitle
        holder.artistName.text = artist.artistName
    }

    override fun getItemCount(): Int = artistList.size
}
