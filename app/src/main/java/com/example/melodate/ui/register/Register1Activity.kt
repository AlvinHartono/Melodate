package com.example.melodate.ui.register

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.melodate.databinding.ActivityRegsiter1Binding

class Register1Activity : AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityRegsiter1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegsiter1Binding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.fabBack.setOnClickListener {
            finish()
        }
    }
}