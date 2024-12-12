package com.example.melodate.ui.dashboard

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melodate.R
import com.example.melodate.data.remote.response.MatchCard
import com.example.melodate.ui.spotify.SpotifyViewModel
import com.google.android.flexbox.FlexboxLayout

class CardStackAdapter(
    private val cards: List<MatchCard>,
    private val spotifyViewModel: SpotifyViewModel
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.profile_name)
        val imageButton: ImageButton = view.findViewById(R.id.play_button)
        val songTitle: TextView = view.findViewById(R.id.song_title)
        val artistName: TextView = view.findViewById(R.id.artist_name)
        val description: TextView = view.findViewById(R.id.profile_description)
        val image1: ImageView = view.findViewById(R.id.profile_image1)
        val image2: ImageView = view.findViewById(R.id.profile_image2)
        val image3: ImageView = view.findViewById(R.id.profile_image3)
        val image4: ImageView = view.findViewById(R.id.profile_image4)
        val image5: ImageView = view.findViewById(R.id.profile_image5)
        val image6: ImageView = view.findViewById(R.id.profile_image6)

        val location: TextView = view.findViewById(R.id.location)
        val descriptionList: FlexboxLayout = view.findViewById(R.id.description_list)
        val musicInterestList: FlexboxLayout = view.findViewById(R.id.music_interest_list)

        val favoriteArtistsRecyclerView: RecyclerView =
            view.findViewById(R.id.favorite_artists_recycler_view)
        val topSongsRecyclerView: RecyclerView = view.findViewById(R.id.top_songs_recycler_view)
    }


    fun getItem(position: Int): MatchCard {
        return cards[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]

        holder.imageButton.setOnClickListener {
            val songTitle = card.topTrackTitle1 ?: "Default Title"
            val artistName = card.topTrackArtist1 ?: "Default Artist"
            val query = "$songTitle $artistName"

            spotifyViewModel.setCurrentQuery(query)

            spotifyViewModel.fetchTrackUrl { trackUrl ->
                if (trackUrl != null) {
                    Log.d("SpotifyAPI", "Track URL: $trackUrl")
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(trackUrl)
                        `package` = "com.spotify.music"
                    }
                    spotifyViewModel.setCurrentQuery(null)
                    if (intent.resolveActivity(holder.itemView.context.packageManager) != null) {
                        holder.itemView.context.startActivity(intent)
                    } else {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trackUrl))
                        holder.itemView.context.startActivity(browserIntent)
                    }
                } else {
                    spotifyViewModel.setCurrentQuery(null)
                }
            }
        }

        if(card.topTrackTitle1.isNullOrEmpty() && card.topTrackArtist1.isNullOrEmpty()){
            holder.itemView.findViewById<LinearLayout>(R.id.music_play).visibility = View.GONE
        }else{
            holder.itemView.findViewById<LinearLayout>(R.id.music_play).visibility = View.VISIBLE
            holder.songTitle.text = card.topTrackTitle1
            holder.artistName.text = card.topTrackArtist1
        }

        val favoriteArtists = listOfNotNull(
            card.topArtistImage1?.let { FavoriteArtistData(it, card.topArtistName1) },
            card.topArtistImage2?.let { FavoriteArtistData(it, card.topArtistName2) },
            card.topArtistImage3?.let { FavoriteArtistData(it, card.topArtistName3) }
        )

        val topSongs = listOfNotNull(
            card.topTrackImage1?.let { TopSongData(it, card.topTrackTitle1, card.topTrackArtist1) },
            card.topTrackImage2?.let { TopSongData(it, card.topTrackTitle2, card.topTrackArtist2) },
            card.topTrackImage3?.let { TopSongData(it, card.topTrackTitle3, card.topTrackArtist3) },
            card.topTrackImage4?.let { TopSongData(it, card.topTrackTitle4, card.topTrackArtist4) },
            card.topTrackImage5?.let { TopSongData(it, card.topTrackTitle5, card.topTrackArtist5) }
        )

        holder.name.text = holder.itemView.context.getString(R.string.name_and_age, card.firstName, card.age)
        if(card.biodata.isNullOrEmpty()){
            holder.description.visibility = View.GONE
        }else{
            holder.description.visibility = View.VISIBLE
            holder.description.text = card.biodata
        }
        if (card.location.isNullOrEmpty()) {
            holder.itemView.findViewById<TextView>(R.id.header_location).visibility = View.GONE
            holder.location.visibility = View.GONE
        } else {
            holder.itemView.findViewById<TextView>(R.id.header_location).visibility = View.VISIBLE
            holder.location.visibility = View.VISIBLE
            holder.location.text = card.location
        }

        Glide.with(holder.itemView.context)
            .load(card.profilePictureUrl1)
            .placeholder(R.drawable.loading_state)
            .error(R.drawable.baseline_person_24)
            .into(holder.image1)

        if (!card.profilePictureUrl2.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(card.profilePictureUrl2)
                .placeholder(R.drawable.loading_state)
                .error(R.drawable.baseline_person_24)
                .into(holder.image2)
            holder.image2.visibility = View.VISIBLE
        } else {
            holder.image2.visibility = View.GONE
        }

        if (!card.profilePictureUrl3.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(card.profilePictureUrl3)
                .placeholder(R.drawable.loading_state)
                .error(R.drawable.baseline_person_24)
                .into(holder.image3)
            holder.image3.visibility = View.VISIBLE
        } else {
            holder.image3.visibility = View.GONE
        }

        if (!card.profilePictureUrl4.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(card.profilePictureUrl4)
                .placeholder(R.drawable.loading_state)
                .error(R.drawable.baseline_person_24)
                .into(holder.image4)
            holder.image4.visibility = View.VISIBLE
        } else {
            holder.image4.visibility = View.GONE
        }

        if (!card.profilePictureUrl5.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(card.profilePictureUrl5)
                .placeholder(R.drawable.loading_state)
                .error(R.drawable.baseline_person_24)
                .into(holder.image5)
            holder.image5.visibility = View.VISIBLE
        } else {
            holder.image5.visibility = View.GONE
        }

        if (!card.profilePictureUrl6.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(card.profilePictureUrl6)
                .placeholder(R.drawable.loading_state)
                .error(R.drawable.baseline_person_24)
                .into(holder.image6)
            holder.image6.visibility = View.VISIBLE
        } else {
            holder.image6.visibility = View.GONE
        }

        if (favoriteArtists.isEmpty()) {
            holder.itemView.findViewById<TextView>(R.id.header_fav_artist).visibility = View.GONE
            holder.favoriteArtistsRecyclerView.visibility = View.GONE
        } else {
            holder.itemView.findViewById<TextView>(R.id.header_fav_artist).visibility = View.VISIBLE
            holder.favoriteArtistsRecyclerView.visibility = View.VISIBLE
            val favoriteArtistsAdapter = FavoriteArtistAdapter(favoriteArtists)
            holder.favoriteArtistsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.favoriteArtistsRecyclerView.adapter = favoriteArtistsAdapter
        }

        if (topSongs.isEmpty()) {
            holder.itemView.findViewById<TextView>(R.id.header_top_song).visibility = View.GONE
            holder.topSongsRecyclerView.visibility = View.GONE
        } else {
            holder.itemView.findViewById<TextView>(R.id.header_top_song).visibility = View.VISIBLE
            holder.topSongsRecyclerView.visibility = View.VISIBLE
            val topSongsAdapter = TopSongAdapter(topSongs)
            holder.topSongsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.topSongsRecyclerView.adapter = topSongsAdapter
        }

        val descriptions = listOfNotNull(
            card.gender,
            card.height,
            card.religion,
            card.smokes,
            card.drinks,
            card.education,
            card.status
        ).joinToString(" ")

        holder.descriptionList.removeAllViews()
        val chips = createChipsWithIcons(
            holder.descriptionList.context,
            descriptions,
            holder.descriptionList
        )
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
            val chip = createChipForMusicInterest(
                holder.musicInterestList.context,
                interest,
                holder.musicInterestList
            )
            holder.musicInterestList.addView(chip)
        }
    }

    override fun getItemCount(): Int = cards.size

    private fun createChipForMusicInterest(
        context: Context,
        musicInterest: String,
        parent: ViewGroup
    ): View {
        val chipView = LayoutInflater.from(context)
            .inflate(R.layout.item_chip_music, parent, false) as TextView

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

    private fun createChipsWithIcons(
        context: Context,
        description: String,
        parent: ViewGroup
    ): List<View> {
        val iconMappingLight = mapOf(
            "Gender" to R.drawable.gender,
            "Height" to R.drawable.height_dark,
            "Religion" to R.drawable.religion,
            "Smoke" to R.drawable.smoke,
            "Drink" to R.drawable.drink,
            "Education" to R.drawable.education,
            "Status" to R.drawable.status
        )

        val iconMappingDark = mapOf(
            "Gender" to R.drawable.gender_light,
            "Height" to R.drawable.height_light,
            "Religion" to R.drawable.religion_light,
            "Smoke" to R.drawable.smoke_light,
            "Drink" to R.drawable.drink_light,
            "Education" to R.drawable.education_light,
            "Status" to R.drawable.status_light
        )

        val isDarkMode =
            when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                else -> false
            }

        val iconMapping = if (isDarkMode) iconMappingDark else iconMappingLight

        val categoryConditions = listOf(
            "Gender" to listOf("Male", "Female"),
            "Height" to emptyList(),
            "Religion" to listOf("Islam", "Christian", "Roman Catholicism", "Buddhist", "Hindu", "Other"),
            "Smoke" to listOf("Smoke", "Always", "Often", "Rarely", "Never", "No"),
            "Drink" to listOf("Drink", "Always", "Often", "Rarely", "Never", "No"),
            "Education" to listOf("Graduate", "Undergraduate", "No Degree", "Sarjana"),
            "Status" to listOf("Single", "Taken")
        )

        val heightPattern = "\\b(\\d{2,3}\\s?(cm|CM|Cm)?|(\\d'\\d{1,2}\")|(\\d{1,2}\"))\\b".toRegex()

        return categoryConditions.mapNotNull { (category, keywords) ->
            val matchedKeyword = when (category) {
                "Height" -> heightPattern.find(description)?.value
                else -> keywords.sortedByDescending { it.length }
                    .find { keyword ->
                        "\\b${Regex.escape(keyword)}\\b".toRegex(RegexOption.IGNORE_CASE)
                            .containsMatchIn(description)
                    }
            }

            matchedKeyword?.let { keyword ->
                val chipView = LayoutInflater.from(context)
                    .inflate(R.layout.item_chip_description, parent, false) as TextView
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






