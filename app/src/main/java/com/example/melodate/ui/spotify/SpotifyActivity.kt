package com.example.melodate.ui.spotify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.melodate.R
import com.example.melodate.data.di.Injection
import com.example.melodate.data.preference.TopArtistsAdapter
import com.example.melodate.data.preference.TopTracksAdapter
import com.spotify.sdk.android.auth.AuthorizationResponse

class SpotifyActivity : AppCompatActivity() {

    private val spotifyViewModel: SpotifyViewModel by viewModels {
        SpotifyViewModelFactory(
            Injection.provideSpotifyRepository(),
            Injection.provideSpotifyPreference(this)
        )
    }

    private lateinit var topArtistsAdapter: TopArtistsAdapter
    private lateinit var topTracksAdapter: TopTracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spotify)

        setupRecyclerViews()

        spotifyViewModel.topArtists.observe(this) { artists ->
            topArtistsAdapter.submitList(artists)
        }

        spotifyViewModel.topTracks.observe(this) { tracks ->
            topTracksAdapter.submitList(tracks)
        }

        spotifyViewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        fetchSpotifyData()
    }

    private fun setupRecyclerViews() {
        val artistsRecyclerView = findViewById<RecyclerView>(R.id.rv_top_artists)
        topArtistsAdapter = TopArtistsAdapter()
        artistsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        artistsRecyclerView.adapter = topArtistsAdapter

        val tracksRecyclerView = findViewById<RecyclerView>(R.id.rv_top_tracks)
        topTracksAdapter = TopTracksAdapter()
        tracksRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksRecyclerView.adapter = topTracksAdapter
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("SpotifyCallback", "onNewIntent triggered in ${this::class.java.simpleName}")
        intent.data?.let { uri ->
            Log.d("SpotifyCallback", "Received URI: $uri")
            if (uri.host == "callback") {
                val response = AuthorizationResponse.fromUri(uri)
                Log.d("SpotifyCallback", "AuthorizationResponse: $response")
                handleSpotifyResponse(response)
            }
        } ?: Log.d("SpotifyCallback", "Intent data is null")
    }

    private fun handleSpotifyResponse(response: AuthorizationResponse) {
        when (response.type) {
            AuthorizationResponse.Type.TOKEN -> {
                val token = response.accessToken
                Log.d("SpotifyToken", "Access token: $token")
                spotifyViewModel.saveSpotifyToken(token)
                fetchSpotifyData()
            }
            AuthorizationResponse.Type.ERROR -> {
                Toast.makeText(this, "Spotify Error: ${response.error}", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Unknown Spotify response", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchSpotifyData() {
        spotifyViewModel.fetchTopArtists()
        spotifyViewModel.fetchTopTracks()
    }
}
