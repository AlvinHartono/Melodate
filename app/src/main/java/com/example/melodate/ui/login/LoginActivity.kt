package com.example.melodate.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.melodate.HomeActivity
import com.example.melodate.databinding.ActivityLoginBinding
import com.example.melodate.ui.register.RegisterEmailPasswordActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonLogin.setOnClickListener {
            //intent to register
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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
}