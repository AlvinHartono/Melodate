package com.example.melodate.ui.dashboard

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.melodate.R
import com.google.android.flexbox.FlexboxLayout

class CardStackAdapter(
    private val cards: List<CardData>
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.profile_name)
        val songTitle: TextView = view.findViewById(R.id.song_title)
        val artistName: TextView = view.findViewById(R.id.artist_name)
        val description: TextView = view.findViewById(R.id.profile_description)
        val image: ImageView = view.findViewById(R.id.profile_image)
        val location: TextView = view.findViewById(R.id.location)
        val descriptionList: FlexboxLayout = view.findViewById(R.id.description_list)
        val musicInterestList: FlexboxLayout = view.findViewById(R.id.music_interest_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]
        holder.name.text = card.name
        holder.songTitle.text = card.songTitle
        holder.artistName.text = card.artistName
        holder.description.text = card.description
        holder.image.setImageResource(card.image)
        holder.location.text = card.location

        holder.descriptionList.removeAllViews()
        card.descriptionList.forEach { description ->
            val chip = createChipWithIcon(holder.descriptionList.context, description,  holder.descriptionList)
            holder.descriptionList.addView(chip)
        }

        holder.musicInterestList.removeAllViews()
        card.musicInterest.forEach { interest ->
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

    private fun createChipWithIcon(context: Context, description: String, parent: ViewGroup): View {
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

        val category = when {
            description.contains("Male", ignoreCase = true) || description.contains("Female", ignoreCase = true) -> "Gender"
            listOf("Muslim", "Christian", "Catholic", "Buddhist", "Hindu", "Other").any { description.contains(it, ignoreCase = true) } -> "Religion"
            listOf("Always", "Often", "Rarely", "Never").any { description.contains(it, ignoreCase = true) } -> "Smoke"
            listOf("Always", "Often", "Rarely", "Never").any { description.contains(it, ignoreCase = true) } -> "Drink"
            listOf("Graduate", "Undergraduate", "No Degree").any { description.contains(it, ignoreCase = true) } -> "Education"
            description.contains("Single", ignoreCase = true) || description.contains("Taken", ignoreCase = true) -> "Status"
            else -> null
        }

        val chipView = LayoutInflater.from(context).inflate(R.layout.item_chip_description, parent, false) as TextView

        chipView.text = description

        category?.let {
            val iconResId = iconMapping[it]
            if (iconResId != null) {
                chipView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0)
            }
        }

        val params = FlexboxLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 30, 35)
        }
        chipView.layoutParams = params

        return chipView
    }
}




