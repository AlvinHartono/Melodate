package com.example.melodate.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.melodate.MainActivity
import com.example.melodate.databinding.ActivityRegisterEmailPasswordBinding

import com.example.melodate.ui.login.LoginActivity

class RegisterEmailPasswordActivity : AppCompatActivity() {
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
            val intent = Intent(this@RegisterEmailPasswordActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this@RegisterEmailPasswordActivity, LoginActivity::class.java)
            startActivity(intent)

        }
    }
}