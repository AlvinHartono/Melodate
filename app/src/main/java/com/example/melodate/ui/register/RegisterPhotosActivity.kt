package com.example.melodate.ui.register

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.melodate.R
import com.example.melodate.databinding.ActivityRegisterPhotosBinding

class RegisterPhotosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPhotosBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPhotosBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fabBack.setOnClickListener {
            finish()
        }
        binding.buttonRegister.setOnClickListener {
            //register user
            //if loading, show loading
            // if failed, show alert box
            // if good, intent to register finished activity
            //show the loading for 2 seconds
            binding.progressBar.visibility = View.VISIBLE
            Handler().postDelayed({
                binding.progressBar.visibility = View.GONE
            }, 2000)

            val intent = Intent(this@RegisterPhotosActivity, RegisterFinishedActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}