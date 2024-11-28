package com.example.melodate.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.melodate.R
import com.example.melodate.databinding.ActivityRegisterMusicPreferenceBinding
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory

class RegisterMusicPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterMusicPreferenceBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onResume() {
        super.onResume()
        setupDropDowns()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterMusicPreferenceBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
        setupObservers()


    }

    private fun setupObservers() {
        authViewModel.userData.observe(this) { userData ->
            if (userData.genre != null) {
                binding.autoCompleteGenre.setText(userData.genre)
            }
            if (userData.musicDecade != null) {
                binding.autoCompleteMusicDecade.setText(userData.musicDecade)
            }
            if (userData.musicVibe != null) {
                binding.autoCompleteMusicVibe.setText(userData.musicVibe)
            }
            if (userData.listeningFrequency != null) {
                binding.autoCompleteListeningFrequency.setText(userData.listeningFrequency)
            }
            if (userData.concert != null) {
                binding.autoCompleteConcert.setText(userData.concert)
            }
        }
        authViewModel.genre.observe(this) { genre ->
            if (binding.autoCompleteGenre.text.toString() != genre) {
                binding.autoCompleteGenre.setText(genre)
            }
        }
        authViewModel.musicDecade.observe(this) { musicDecade ->
            if (binding.autoCompleteMusicDecade.text.toString() != musicDecade) {
                binding.autoCompleteMusicDecade.setText(musicDecade)
            }
        }
        authViewModel.musicVibe.observe(this) { musicVibe ->
            if (binding.autoCompleteMusicVibe.text.toString() != musicVibe) {
                binding.autoCompleteMusicVibe.setText(musicVibe)
            }
        }

        authViewModel.listeningFreq.observe(this) { listeningFreq ->
            if (binding.autoCompleteListeningFrequency.text.toString() != listeningFreq) {
                binding.autoCompleteListeningFrequency.setText(listeningFreq)
            }
        }
        authViewModel.concert.observe(this) { concert ->
            if (binding.autoCompleteConcert.text.toString() != concert) {
                binding.autoCompleteConcert.setText(concert)
            }
        }
    }

    private fun setupListeners() {

        binding.autoCompleteGenre.addTextChangedListener { text ->
            authViewModel.setGenre(text.toString())
        }
        binding.autoCompleteMusicDecade.addTextChangedListener { text ->
            authViewModel.setMusicDecade(text.toString())
        }
        binding.autoCompleteMusicVibe.addTextChangedListener { text ->
            authViewModel.setMusicVibe(text.toString())
        }
        binding.autoCompleteListeningFrequency.addTextChangedListener { text ->
            authViewModel.setListeningFreq(text.toString())
        }
        binding.autoCompleteConcert.addTextChangedListener { text ->
            authViewModel.setConcert(text.toString())
        }

        binding.fabForward.setOnClickListener {
            val genre = binding.autoCompleteGenre.text.toString()
            val musicDecade = binding.autoCompleteMusicDecade.text.toString()
            val musicVibe = binding.autoCompleteMusicVibe.text.toString()
            val listeningFreq = binding.autoCompleteListeningFrequency.text.toString()
            val concert = binding.autoCompleteConcert.text.toString()

            authViewModel.updateMusicPreferenceData(
                genre,
                musicDecade,
                musicVibe,
                listeningFreq,
                concert
            )
            val intent =
                Intent(this@RegisterMusicPreferenceActivity, RegisterPhotosActivity::class.java)
            startActivity(intent)
        }
        binding.fabBack.setOnClickListener {
            finish()
        }
    }

    private fun setupDropDowns() {
        val genres = resources.getStringArray(R.array.music_genres)
        val musicDecade = resources.getStringArray(R.array.music_decades)
        val musicVibe = resources.getStringArray(R.array.music_vibes)
        val listeningFrequency = resources.getStringArray(R.array.listening_frequency)
        val concert = resources.getStringArray(R.array.concert_preferences)


        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, genres)
        binding.autoCompleteGenre.setAdapter(arrayAdapter)
        val arrayAdapter2 = ArrayAdapter(this, R.layout.dropdown_item, musicDecade)
        binding.autoCompleteMusicDecade.setAdapter(arrayAdapter2)
        val arrayAdapter3 = ArrayAdapter(this, R.layout.dropdown_item, musicVibe)
        binding.autoCompleteMusicVibe.setAdapter(arrayAdapter3)
        val arrayAdapter4 = ArrayAdapter(this, R.layout.dropdown_item, listeningFrequency)
        binding.autoCompleteListeningFrequency.setAdapter(arrayAdapter4)
        val arrayAdapter5 = ArrayAdapter(this, R.layout.dropdown_item, concert)
        binding.autoCompleteConcert.setAdapter(arrayAdapter5)
    }
}