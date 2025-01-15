package com.example.melodate.ui.melodate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melodate.R

class FavoriteArtistAdapter(
    private val artistList: List<FavoriteArtistData>
) : RecyclerView.Adapter<FavoriteArtistAdapter.FavoriteArtistViewHolder>() {

    inner class FavoriteArtistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val artistImage: ImageView = view.findViewById(R.id.artist_image)
        val artistName: TextView = view.findViewById(R.id.artist_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)
        return FavoriteArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteArtistViewHolder, position: Int) {
        val artist = artistList[position]

        Glide.with(holder.itemView.context)
            .load(artist.songImage)
            .placeholder(R.drawable.newjeans)
            .error(R.drawable.baseline_person_24)
            .into(holder.artistImage)

        holder.artistName.text = artist.artistName
    }

    override fun getItemCount(): Int = artistList.size
}

