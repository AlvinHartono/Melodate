package com.example.melodate

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.melodate.databinding.ActivityHomeBinding
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

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
                    val isDarkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
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

        //fetch users data
        authViewModel.getUserData()
    }
}

