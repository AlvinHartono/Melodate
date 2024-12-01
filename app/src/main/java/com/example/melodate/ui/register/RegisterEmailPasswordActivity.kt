package com.example.melodate.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        setupListeners()
        setupObservers()
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
                    Toast.makeText(this, isAvailable.error, Toast.LENGTH_SHORT)
                        .show()
                }

                Result.Loading -> {
                    showLoading(true)
                } //Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
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
                        startActivity(intent)
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

            //check if email is not empty
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All the fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                authViewModel.checkEmail(email)
//                authViewModel.saveUserEntity(UserEntity(email = email, password = password))
//                //intent
//                val intent =
//                    Intent(this@RegisterEmailPasswordActivity, RegisterUserPersonalDataActivity::class.java)
//                startActivity(intent)
            }

        }

    }


}