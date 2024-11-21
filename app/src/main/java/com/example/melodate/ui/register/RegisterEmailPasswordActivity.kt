package com.example.melodate.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.melodate.databinding.ActivityRegisterEmailPasswordBinding

import com.example.melodate.ui.login.LoginActivity

class RegisterEmailPasswordActivity : AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityRegisterEmailPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterEmailPasswordBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.fabBack.setOnClickListener {
            finish()
        }

        binding.tvLogin.setOnClickListener {
            //intent to login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }
}