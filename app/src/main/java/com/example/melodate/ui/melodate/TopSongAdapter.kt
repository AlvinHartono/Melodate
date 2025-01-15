package com.example.melodate.ui.melodate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melodate.R

class TopSongAdapter(
    private val songList: List<TopSongData>
) : RecyclerView.Adapter<TopSongAdapter.TopSongViewHolder>() {

    inner class TopSongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songImage: ImageView = view.findViewById(R.id.song_image)
        val songTitle: TextView = view.findViewById(R.id.song_title)
        val songArtist: TextView = view.findViewById(R.id.song_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopSongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return TopSongViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopSongViewHolder, position: Int) {
        val song = songList[position]

        Glide.with(holder.itemView.context)
            .load(song.artistImage)
            .placeholder(R.drawable.apt)
            .error(R.drawable.baseline_person_24)
            .into(holder.songImage)

        holder.songTitle.text = song.songTitle
        holder.songArtist.text = song.artistName
    }

    override fun getItemCount(): Int = songList.size
}
