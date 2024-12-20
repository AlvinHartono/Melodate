package com.example.melodate.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.addTextChangedListener
import com.example.melodate.R
import com.example.melodate.data.Result
import com.example.melodate.data.local.database.UserEntity
import com.example.melodate.databinding.ActivityRegisterEmailPasswordBinding
import com.example.melodate.ui.login.LoginActivity
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory

class RegisterEmailPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterEmailPasswordBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterEmailPasswordBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupIcons()
        setupListeners()
        setupObservers()
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

        val invisible = AppCompatResources.getDrawable(this, R.drawable.ic_invisible)

        binding.etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(
            emailIcon, null, null, null
        )
        binding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
            passwordIcon, null, invisible, null
        )
        binding.etConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
            passwordIcon, null, invisible, null
        )
    }

    private fun setupObservers() {
        authViewModel.email.observe(this) { email ->
            if (binding.etEmail.text.toString() != email) {
                binding.etEmail.setText(email)
            }
        }
        authViewModel.password.observe(this) { password ->
            if (binding.etPassword.text.toString() != password) {
                binding.etPassword.setText(password)
            }
        }
        authViewModel.confirmPassword.observe(this) { confirmPassword ->
            if (binding.etConfirmPassword.text.toString() != confirmPassword) {
                binding.etConfirmPassword.setText(confirmPassword)
            }
        }

        authViewModel.isEmailAvailable.observe(this) { isAvailable ->
            when (isAvailable) {
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_SHORT)
                        .show()
                }

                Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    if (isAvailable.data) {
                        showLoading(false)
                        val email = authViewModel.email.value.toString()
                        val password = authViewModel.password.value.toString()
                        authViewModel.saveUserEntity(UserEntity(email = email, password = password))

                        val intent =
                            Intent(
                                this@RegisterEmailPasswordActivity,
                                RegisterUserPersonalDataActivity::class.java
                            )

                        val options = ActivityOptionsCompat.makeCustomAnimation(
                            this@RegisterEmailPasswordActivity,
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )

                        startActivity(intent, options.toBundle())
                    } else {
                        showLoading(false)
                        Toast.makeText(this, "Email already taken", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.buttonRegister.isEnabled = false
            binding.buttonRegister.text = ""
        } else {
            binding.progressBar.visibility = View.GONE
            binding.buttonRegister.isEnabled = true
            binding.buttonRegister.text = getString(R.string.register)
        }

    }

    private fun setupListeners() {

        binding.etEmail.addTextChangedListener { text ->
            authViewModel.setEmail(text.toString())
        }
        binding.etPassword.addTextChangedListener { text ->
            authViewModel.setPassword(text.toString())
        }
        binding.etConfirmPassword.addTextChangedListener { text ->
            authViewModel.setConfirmPassword(text.toString())
        }

        binding.fabBack.setOnClickListener {
            finish()
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this@RegisterEmailPasswordActivity, LoginActivity::class.java)
            startActivity(intent)

        }


        binding.buttonRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
            val passwordRegex =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$".toRegex()

            //check if email is not empty
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All the fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if email format is valid
            if (!email.matches(emailRegex)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Check if password is strong
            if (!password.matches(passwordRegex)) {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Password must contain at least 8 characters, including uppercase and lowercase letters, numbers, and special characters.")
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()

                return@setOnClickListener
            }

            if(password != confirmPassword){
                Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.checkEmail(email)

        }

    }


}