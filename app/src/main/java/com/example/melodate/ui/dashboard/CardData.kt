package com.example.melodate.ui.dashboard

data class CardData(
    val name: String,
    val songTitle: String,
    val artistName: String,
    val description: String,
    val image: Int,
    val musicInterest: List<String>,
    val descriptionList: List<String>,
    val location: String,
    val favoriteArtists: List<FavoriteArtistData>,
    val topSongs: List<TopSongData>
)


