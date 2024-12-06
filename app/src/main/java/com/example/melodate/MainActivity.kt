package com.example.melodate

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.melodate.data.preference.DarkModeViewModel
import com.example.melodate.data.preference.DarkModeViewModelFactory
import com.example.melodate.data.preference.SettingPreferences
import com.example.melodate.data.preference.dataStore
import com.example.melodate.databinding.ActivityMainBinding
import com.example.melodate.ui.login.LoginActivity
import com.example.melodate.ui.register.RegisterEmailPasswordActivity
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }
    private val darkModeViewModel: DarkModeViewModel by viewModels {
        DarkModeViewModelFactory(SettingPreferences(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            Thread.sleep(2000)
            false
        }
        super.onCreate(savedInstanceState)

        darkModeViewModel.isDarkMode.observe(this) { isDarkMode ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        lifecycleScope.launch {
            authViewModel.authToken.collect { token ->
                if (token != null) {
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    binding = ActivityMainBinding.inflate(layoutInflater)
                    setContentView(binding.root)
                    enableEdgeToEdge()

                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                        )
                        insets
                    }

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



                    binding.imageView.setImageResource(imageRes)

                    binding.buttonGetStarted.setOnClickListener {
                        val intent =
                            Intent(this@MainActivity, RegisterEmailPasswordActivity::class.java)
//                            Intent(this@MainActivity, HomeActivity::class.java)
//                            Intent(this@MainActivity, RegisterFinishedActivity::class.java)
//                        Intent(this@MainActivity, RegisterUserPersonalDataActivity::class.java)
                        startActivity(intent)
                    }

                    binding.tvHaveAccount.setOnClickListener {
//                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

}