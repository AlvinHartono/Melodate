package com.example.melodate.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.melodate.R
import com.example.melodate.data.Result
import com.example.melodate.databinding.ActivityLoginBinding
import com.example.melodate.ui.register.RegisterEmailPasswordActivity
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupIcons()
        setupListeners()
        setupObservers()
    }


    private fun setupObservers() {
        binding.etEmail.addTextChangedListener {
            authViewModel.setEmail(it.toString())
        }

        binding.etPassword.addTextChangedListener {
            authViewModel.setPassword(it.toString())
        }

        authViewModel.email.observe(this) {
            if (binding.etEmail.text.toString() != it) {
                binding.etEmail.setText(it)
            }
        }
        authViewModel.password.observe(this) {
            if (binding.etPassword.text.toString() != it) {
                binding.etPassword.setText(it)
            }
        }
        authViewModel.loginState.observe(this) {
            when (it) {
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
                Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.buttonLogin.isEnabled = false
            binding.buttonLogin.text = ""
        } else {
            binding.progressBar.visibility = View.GONE
            binding.buttonLogin.isEnabled = true
            binding.buttonLogin.text = getString(R.string.register)
        }

    }

    private fun setupListeners() {
        binding.buttonLogin.setOnClickListener {

            val email = authViewModel.email.value.orEmpty()
            val password = authViewModel.password.value.orEmpty()

            if (password.length >= 6) {
                lifecycleScope.launch {
                    authViewModel.login(email, password)
                }
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Password harus lebih dari 8 karakter",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            //intent to register
            val intent = Intent(this, RegisterEmailPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.fabBack.setOnClickListener {
            finish()
        }

    }

    private fun setupIcons() {
        val isDarkTheme = (resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES

        val emailIcon = if (isDarkTheme) {
            AppCompatResources.getDrawable(this, R.drawable.ic_email_dark)
        } else {
            AppCompatResources.getDrawable(this, R.drawable.ic_email_light)
        }

        val passwordIcon = if (isDarkTheme) {
            AppCompatResources.getDrawable(this, R.drawable.ic_password_dark)
        } else {
            AppCompatResources.getDrawable(this, R.drawable.ic_password_light)
        }

        binding.etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(
            emailIcon, null, null, null
        )
        binding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
            passwordIcon, null, null, null
        )
    }
}