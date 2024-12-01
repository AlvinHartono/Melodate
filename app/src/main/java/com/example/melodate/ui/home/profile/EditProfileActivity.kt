package com.example.melodate.ui.home.profile

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.melodate.R
import com.example.melodate.databinding.ActivityEditProfileBinding
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onResume() {
        super.onResume()
        setupDropDowns()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //add back button
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Show the back button
            setDisplayShowHomeEnabled(true) // Ensure it is visible
        }

//        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        authViewModel.userData.observe(this) { user ->
            if (user != null) {
                binding.editTextBio.setText(user.bio)
                binding.editTextHeight.setText(user.height.toString())
                binding.editTextLocation.setText(user.location)
                binding.autoCompleteRelationshipStatus.setText(user.status)
                binding.autoCompleteEducation.setText(user.education)
                binding.autoCompleteReligion.setText(user.religion)
                binding.autoCompleteSmoking.setText(if (user.isSmoker!!) "Yes" else "No")
                binding.autoCompleteDrinking.setText(if (user.isDrinker!!) "Yes" else "No")
                binding.autoCompleteMbti.setText(user.mbti)
                binding.autoCompleteLoveLanguage.setText(user.loveLang)
                binding.autoCompleteGenre.setText(user.genre)
                binding.autoCompleteMusicDecade.setText(user.musicDecade)
                binding.autoCompleteMusicVibe.setText(user.musicVibe)
                binding.autoCompleteListeningFrequency.setText(user.listeningFrequency)
                binding.autoCompleteConcert.setText(user.concert)
            }
        }
    }

    private fun setupListeners() {
        binding.editTextLocation.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions if not already granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1001 // Request code for location permissions
            )
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    ?.toList() // Convert MutableList to List

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val province = address.adminArea ?: "Unknown Province"
                    val country = address.countryName ?: "Unknown Country"
                    binding.editTextLocation.setText("$province, $country")
                }
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to detect location: ${it.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupDropDowns() {
        val status = resources.getStringArray(R.array.status)
        val religion = resources.getStringArray(R.array.religion)
        val education = resources.getStringArray(R.array.education)
        val smoking = resources.getStringArray(R.array.smoking)
        val drinking = resources.getStringArray(R.array.drinking)
        val mbti = resources.getStringArray(R.array.MBTI)
        val loveLanguages = resources.getStringArray(R.array.love_languages)
        val genres = resources.getStringArray(R.array.music_genres)
        val musicDecade = resources.getStringArray(R.array.music_decades)
        val musicVibe = resources.getStringArray(R.array.music_vibes)
        val listeningFrequency = resources.getStringArray(R.array.listening_frequency)
        val concert = resources.getStringArray(R.array.concert_preferences)


        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, status)
        binding.autoCompleteRelationshipStatus.setAdapter(arrayAdapter)
        val arrayAdapter2 = ArrayAdapter(this, R.layout.dropdown_item, religion)
        binding.autoCompleteReligion.setAdapter(arrayAdapter2)
        val arrayAdapter3 = ArrayAdapter(this, R.layout.dropdown_item, education)
        binding.autoCompleteEducation.setAdapter(arrayAdapter3)
        val arrayAdapter4 = ArrayAdapter(this, R.layout.dropdown_item, smoking)
        binding.autoCompleteSmoking.setAdapter(arrayAdapter4)
        val arrayAdapter5 = ArrayAdapter(this, R.layout.dropdown_item, drinking)
        binding.autoCompleteDrinking.setAdapter(arrayAdapter5)
        val arrayAdapter6 = ArrayAdapter(this, R.layout.dropdown_item, mbti)
        binding.autoCompleteMbti.setAdapter(arrayAdapter6)
        val arrayAdapter7 = ArrayAdapter(this, R.layout.dropdown_item, loveLanguages)
        binding.autoCompleteLoveLanguage.setAdapter(arrayAdapter7)
        val arrayAdapter8 = ArrayAdapter(this, R.layout.dropdown_item, mbti)
        binding.autoCompleteMbti.setAdapter(arrayAdapter8)
        val arrayAdapter9 = ArrayAdapter(this, R.layout.dropdown_item, loveLanguages)
        binding.autoCompleteLoveLanguage.setAdapter(arrayAdapter9)
        val arrayAdapter10 = ArrayAdapter(this, R.layout.dropdown_item, genres)
        binding.autoCompleteGenre.setAdapter(arrayAdapter10)
        val arrayAdapter11 = ArrayAdapter(this, R.layout.dropdown_item, musicDecade)
        binding.autoCompleteMusicDecade.setAdapter(arrayAdapter11)
        val arrayAdapter12 = ArrayAdapter(this, R.layout.dropdown_item, musicVibe)
        binding.autoCompleteMusicVibe.setAdapter(arrayAdapter12)
        val arrayAdapter13 = ArrayAdapter(this, R.layout.dropdown_item, listeningFrequency)
        binding.autoCompleteListeningFrequency.setAdapter(arrayAdapter13)
        val arrayAdapter14 = ArrayAdapter(this, R.layout.dropdown_item, concert)
        binding.autoCompleteConcert.setAdapter(arrayAdapter14)


    }
}
