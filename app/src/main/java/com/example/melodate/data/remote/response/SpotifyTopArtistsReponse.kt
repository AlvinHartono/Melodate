package com.example.melodate.data.remote.response

import com.google.gson.annotations.SerializedName

data class SpotifyTopArtistsResponse(
    @SerializedName("items") val artists: List<SpotifyArtist>
)

data class SpotifyArtist(
    @SerializedName("name") val name: String,
    @SerializedName("genres") val genres: List<String>,
    @SerializedName("images") val images: List<SpotifyImage>,
    @SerializedName("followers") val followers: Followers,
    @SerializedName("popularity") val popularity: Int,
    @SerializedName("external_urls") val externalUrls: ExternalUrls
)

data class SpotifyImage(
    @SerializedName("url") val url: String
)

data class Followers(
    @SerializedName("total") val total: Int
)

data class ExternalUrls(
    @SerializedName("spotify") val spotify: String
)

