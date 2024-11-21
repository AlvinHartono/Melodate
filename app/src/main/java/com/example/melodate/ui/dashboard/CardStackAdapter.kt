package com.example.melodate.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.melodate.R

class CardStackAdapter(
    private val data: List<CardData>
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageProfile: ImageView = view.findViewById(R.id.profile_image)
        val textName: TextView = view.findViewById(R.id.profile_name)
        val textSongTitle: TextView = view.findViewById(R.id.song_title)
        val textArtistName: TextView = view.findViewById(R.id.artist_name)
        val textDescription: TextView = view.findViewById(R.id.profile_description)
        val textMusicInterest: TextView = view.findViewById(R.id.music_interest)
        val textLocation: TextView = view.findViewById(R.id.location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textName.text = item.name
        holder.textSongTitle.text = item.song_title
        holder.textArtistName.text = item.artist_name
        holder.textDescription.text = item.description
        holder.textMusicInterest.text = holder.itemView.context.getString(
            R.string.music_interest, item.musicInterest
        )
        holder.textLocation.text = holder.itemView.context.getString(
            R.string.location, item.location
        )
        holder.imageProfile.setImageResource(item.image)
    }

    override fun getItemCount(): Int = data.size
}


