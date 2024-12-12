package com.example.melodate

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.melodate.data.Result
import com.example.melodate.data.di.Injection
import com.example.melodate.data.preference.AuthTokenPreference
import com.example.melodate.databinding.ActivityHomeBinding
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model.UserViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory
import com.example.melodate.ui.shared.view_model_factory.UserViewModelFactory
import com.example.melodate.ui.spotify.SpotifyViewModel
import com.example.melodate.ui.spotify.SpotifyViewModelFactory
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    private val CLIENT_ID: String = "33f84566fd954b529f82d6bd0e42cdc1"
    private val CLIENT_SECRET: String = "b98c8977fd2548d0a12a73cc11f1a2d3"
    private val REDIRECT_URI: String = "myapp://callback"


    private lateinit var binding: ActivityHomeBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    private val spotifyViewModel: SpotifyViewModel by viewModels {
        SpotifyViewModelFactory(
            Injection.provideSpotifyRepository(),
            Injection.provideSpotifyPreference(this)
        )
    }

    private lateinit var authTokenPreference: AuthTokenPreference
    private var isDataUpdated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolbarImage = binding.toolbarImage
        val toolbarText = binding.toolbarText
        val navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_profile,
                R.id.navigation_melodate,
                R.id.navigation_chat
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_profile -> {
                    toolbarImage.visibility = View.GONE
                    toolbarText.visibility = View.VISIBLE
                    toolbarText.text = getString(R.string.profile_text)
                }

                R.id.navigation_melodate -> {
                    toolbarImage.visibility = View.VISIBLE
                    toolbarText.visibility = View.GONE
                    val isDarkMode =
                        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                            Configuration.UI_MODE_NIGHT_YES -> true
                            Configuration.UI_MODE_NIGHT_NO -> false
                            else -> false
                        }
                    val imageRes = if (isDarkMode) {
                        R.drawable.light_icon_full
                    } else {
                        R.drawable.icon_full
                    }
                    toolbarImage.setImageResource(imageRes)
                }

                R.id.navigation_chat -> {
                    toolbarImage.visibility = View.GONE
                    toolbarText.visibility = View.VISIBLE
                    toolbarText.text = getString(R.string.chat_text)
                }
            }
        }

        userViewModel.updateUserLocalDatabaseState.observe(this) { userData ->
            when (userData) {
                is Result.Error -> {
//                    Toast.makeText(this, userData.error, Toast.LENGTH_SHORT).show()
                }

                Result.Loading -> {}
                is Result.Success -> {
//                    Toast.makeText(this, userData.data, Toast.LENGTH_SHORT).show()
                }
            }
        }

        authTokenPreference = Injection.provideAuthTokenPreference(this)

        observeSpotifyData()
        authViewModel.getUserData()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { uri ->
            if (uri.host == "callback") {
                val response = AuthorizationResponse.fromUri(uri)
                when (response.type) {
                    AuthorizationResponse.Type.CODE -> {
                        val code = response.code
                        exchangeAuthorizationCodeForTokens(code)
                    }
                    AuthorizationResponse.Type.ERROR -> {
                        Toast.makeText(this, "Spotify Error: ${response.error}", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Unknown Spotify response", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun observeSpotifyData() {
        spotifyViewModel.topArtists.observe(this) { _ ->
            checkAndUpdateSpotifyData()
        }

        spotifyViewModel.topTracks.observe(this) { _ ->
            checkAndUpdateSpotifyData()
        }

        spotifyViewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            isDataUpdated = false
        }

        lifecycleScope.launch {
            spotifyViewModel.getSpotifyTokenData().collect { tokenData ->
                if (tokenData != null) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime >= tokenData.expiryTime) {
                        Log.d("HomeActivity", "Token refreshed, fetching Spotify data...")
                        spotifyViewModel.refreshSpotifyToken()
                        isDataUpdated = false
                    } else {
                        fetchSpotifyData(tokenData.accessToken)
                    }
                }
            }
        }
    }

    private fun fetchSpotifyData(token: String) {
        spotifyViewModel.fetchTopArtists(token)
        spotifyViewModel.fetchTopTracks(token)
    }

    private fun checkAndUpdateSpotifyData() {
        lifecycleScope.launch {
            authTokenPreference.getUserId().collect { userId ->
                if (userId.isNullOrBlank()) {
                    Log.e("HomeActivity", "User ID is null or empty, skipping update.")
                    return@collect
                }

                val topArtists = spotifyViewModel.topArtists.value
                val topTracks = spotifyViewModel.topTracks.value

                if (!topArtists.isNullOrEmpty() && !topTracks.isNullOrEmpty() && !isDataUpdated) {
                    authViewModel.updateSpotifyData(userId, topArtists, topTracks)
                    isDataUpdated = true
                } else {
                    Log.d("HomeActivity", "Top artists or tracks are missing, skipping update.")
                }
            }
        }
    }

    private fun exchangeAuthorizationCodeForTokens(code: String) {
        val body = "grant_type=authorization_code&code=$code&redirect_uri=$REDIRECT_URI&client_id=$CLIENT_ID"
            .toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())

        val authHeader = "Basic " + Base64.encodeToString(
            "$CLIENT_ID:$CLIENT_SECRET".toByteArray(),
            Base64.NO_WRAP
        )

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    val request = Request.Builder()
                        .url("https://accounts.spotify.com/api/token")
                        .post(body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Authorization", authHeader)
                        .build()

                    OkHttpClient().newCall(request).execute()
                }

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        val json = JSONObject(it)
                        val accessToken = json.getString("access_token")
                        val refreshToken = json.getString("refresh_token")
                        val expiresIn = json.getLong("expires_in")

                        spotifyViewModel.saveSpotifyTokenData(accessToken, refreshToken, expiresIn)
                        fetchSpotifyData(accessToken)
                        Toast.makeText(this@HomeActivity, "Spotify Connected!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Token Exchange Failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

