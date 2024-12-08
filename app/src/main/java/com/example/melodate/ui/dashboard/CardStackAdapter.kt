package com.example.melodate.ui.dashboard

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melodate.R
import com.example.melodate.data.remote.response.MatchCard
import com.google.android.flexbox.FlexboxLayout

class CardStackAdapter(
    private val cards: List<MatchCard>
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.profile_name)
//        val songTitle: TextView = view.findViewById(R.id.song_title)
//        val artistName: TextView = view.findViewById(R.id.artist_name)
        val description: TextView = view.findViewById(R.id.profile_description)
        val image: ImageView = view.findViewById(R.id.profile_image)
        val location: TextView = view.findViewById(R.id.location)
        val descriptionList: FlexboxLayout = view.findViewById(R.id.description_list)
        val musicInterestList: FlexboxLayout = view.findViewById(R.id.music_interest_list)

        val favoriteArtistsRecyclerView: RecyclerView = view.findViewById(R.id.favorite_artists_recycler_view)
        val topSongsRecyclerView: RecyclerView = view.findViewById(R.id.top_songs_recycler_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]

        //data dummy sementara
        val favoriteArtists = listOf(
            FavoriteArtistData(R.drawable.aespa, "AESPA"),
            FavoriteArtistData(R.drawable.aespa, "AESPA"),
            FavoriteArtistData(R.drawable.aespa, "AESPA"),
            FavoriteArtistData(R.drawable.aespa, "AESPA")
        )

        //data dummy sementara
        val topSongs = listOf(
            TopSongData(R.drawable.apt, "APT", "Rose & Bruno"),
            TopSongData(R.drawable.apt, "APT", "Rose & Bruno"),
            TopSongData(R.drawable.apt, "APT", "Rose & Bruno")
        )

        holder.name.text = holder.itemView.context.getString(R.string.name_and_age, card.firstName, card.age)
//        holder.songTitle.text = card.songTitle
//        holder.artistName.text = card.artistName
        holder.description.text = card.biodata
        holder.location.text = card.location

        Glide.with(holder.itemView.context)
            .load(card.profilePictureUrl1)
            .placeholder(R.drawable.haewon)
            .error(R.drawable.baseline_person_24)
            .into(holder.image)

        val favoriteArtistsAdapter = FavoriteArtistAdapter(favoriteArtists)
        holder.favoriteArtistsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.favoriteArtistsRecyclerView.adapter = favoriteArtistsAdapter

        val topSongsAdapter = TopSongAdapter(topSongs)
        holder.topSongsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.topSongsRecyclerView.adapter = topSongsAdapter

        val descriptions = listOfNotNull(
            card.gender,
            card.religion,
            card.smokes,
            card.drinks,
            card.education,
            card.status
        ).joinToString(" ")

        holder.descriptionList.removeAllViews()
        val chips = createChipsWithIcons(holder.descriptionList.context, descriptions, holder.descriptionList)
        chips.forEach { chip ->
            holder.descriptionList.addView(chip)
        }


        val musicInterests = listOfNotNull(
            card.genre,
            card.musicVibe,
            card.musicDecade
        )

        holder.musicInterestList.removeAllViews()
        musicInterests.forEach { interest ->
            val chip = createChipForMusicInterest(holder.musicInterestList.context, interest, holder.musicInterestList)
            holder.musicInterestList.addView(chip)
        }
    }

    override fun getItemCount(): Int = cards.size

    private fun createChipForMusicInterest(context: Context, musicInterest: String, parent: ViewGroup): View {
        val chipView = LayoutInflater.from(context).inflate(R.layout.item_chip_music, parent, false) as TextView

        chipView.text = musicInterest

        val params = FlexboxLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 30, 35)
        }
        chipView.layoutParams = params

        return chipView
    }

    private fun createChipsWithIcons(context: Context, description: String, parent: ViewGroup): List<View> {
        val iconMappingLight = mapOf(
            "Gender" to R.drawable.gender,
            "Religion" to R.drawable.religion,
            "Smoke" to R.drawable.smoke,
            "Drink" to R.drawable.drink,
            "Education" to R.drawable.education,
            "Status" to R.drawable.status
        )

        val iconMappingDark = mapOf(
            "Gender" to R.drawable.gender_light,
            "Religion" to R.drawable.religion_light,
            "Smoke" to R.drawable.smoke_light,
            "Drink" to R.drawable.drink_light,
            "Education" to R.drawable.education_light,
            "Status" to R.drawable.status_light
        )

        val isDarkMode = when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }

        val iconMapping = if (isDarkMode) iconMappingDark else iconMappingLight

        val categoryConditions = listOf(
            "Gender" to listOf("Male", "Female"),
            "Religion" to listOf("Islam", "Christian", "Roman Catholicism", "Buddhist", "Hindu", "Other"),
            "Smoke" to listOf("Smoke", "Always", "Often", "Rarely", "Never", "No"),
            "Drink" to listOf("Drink", "Always", "Often", "Rarely", "Never", "No"),
            "Education" to listOf("Graduate", "Undergraduate", "No Degree", "Sarjana"),
            "Status" to listOf("Single", "Taken")
        )

        return categoryConditions.mapNotNull { (category, keywords) ->
            val matchedKeyword = keywords.sortedByDescending { it.length }
                .find { keyword ->
                    "\\b${Regex.escape(keyword)}\\b".toRegex(RegexOption.IGNORE_CASE).containsMatchIn(description)
                }

            matchedKeyword?.let { keyword ->
                val chipView = LayoutInflater.from(context).inflate(R.layout.item_chip_description, parent, false) as TextView
                chipView.text = keyword

                iconMapping[category]?.let { iconResId ->
                    chipView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0)
                }

                val params = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 30, 35)
                }
                chipView.layoutParams = params

                chipView
            }
        }
    }

}




