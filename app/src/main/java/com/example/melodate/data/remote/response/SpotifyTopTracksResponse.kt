package com.example.melodate.data.remote.response

import com.google.gson.annotations.SerializedName

data class SpotifyTopTracksResponse(
    @SerializedName("items") val tracks: List<SpotifyTrack>
)

data class SpotifyTrack(
    @SerializedName("name") val name: String,
    @SerializedName("album") val album: SpotifyAlbum,
    @SerializedName("artists") val artists: List<SpotifyArtist>,
    @SerializedName("preview_url") val previewUrl: String?,
    @SerializedName("duration_ms") val durationMs: Int,
    @SerializedName("popularity") val popularity: Int,
    @SerializedName("track_number") val trackNumber: Int,
    @SerializedName("disc_number") val discNumber: Int,
    @SerializedName("explicit") val explicit: Boolean,
    @SerializedName("available_markets") val availableMarkets: List<String>
)

data class SpotifyAlbum(
    @SerializedName("name") val name: String,
    @SerializedName("images") val images: List<SpotifyImage>
)

