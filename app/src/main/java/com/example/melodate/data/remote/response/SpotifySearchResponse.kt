package com.example.melodate.data.remote.response

import com.google.gson.annotations.SerializedName

data class SpotifySearchResponse(
    @field:SerializedName("tracks")
    val tracks: TrackResponse
)

data class TrackResponse(
    @field:SerializedName("items")
    val items: List<TrackItem>
)

data class TrackItem(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("artists")
    val artists: List<Artist>,

    @field:SerializedName("external_urls")
    val externalUrls: SpotifyExternalUrls
)

data class Artist(
    @field:SerializedName("name")
    val name: String
)

data class SpotifyExternalUrls(
    @field:SerializedName("spotify")
    val spotify: String
)