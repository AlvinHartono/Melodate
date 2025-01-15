package com.example.melodate.ui.profile.profile

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.melodate.R
import com.example.melodate.data.Result
import com.example.melodate.databinding.ActivityUserProfileBinding
import com.example.melodate.ui.shared.view_model.UserViewModel
import com.example.melodate.ui.shared.view_model_factory.UserViewModelFactory
import kotlin.getValue

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_user_profile)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val userId = intent.getStringExtra(MATCHED_USER_ID)
        if (userId != null) {
            setProfile(userId)
        }


    }

    private fun setProfile(id: String) {
        userViewModel.fetchUserData(id).observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    // alert dialog
                }

                Result.Loading -> {
                    // showLoading(true)
                }

                is Result.Success -> {
                    // showLoading(false)
                    val user = data.data.user
                    if (user != null) {
                        binding.profileName.text = buildString {
                            append(user.firstName)
                            append(", ")
                            append(user.age)
                        }
                        if(user.biodata != null){
                            binding.profileDescription.visibility = android.view.View.VISIBLE
                            binding.profileDescription.text = user.biodata.toString()
                        }
                        binding.status.text = user.status
                        binding.religion.text = user.religion
                        binding.smoke.text = user.smokes

                        binding.gender.text = user.gender
                        binding.education.text = user.education

                        if (user.location  != null){
                            binding.location.text = user.location.toString()
                        }


                        Glide.with(this).load(user.profilePicture1).into(binding.profileImage1)
                        if(user.profilePicture2 != null){
                            Glide.with(this).load(user.profilePicture2).into(binding.profileImage2)
                            binding.profileImage2.visibility = android.view.View.VISIBLE
                        }
                        if(user.profilePicture3 != null){
                            Glide.with(this).load(user.profilePicture3).into(binding.profileImage3)
                            binding.profileImage3.visibility = android.view.View.VISIBLE
                        }
                        if(user.profilePicture4 != null){
                            Glide.with(this).load(user.profilePicture4).into(binding.profileImage4)
                            binding.profileImage4.visibility = android.view.View.VISIBLE
                        }
                        if(user.profilePicture5 != null){
                            Glide.with(this).load(user.profilePicture5).into(binding.profileImage5)
                            binding.profileImage5.visibility = android.view.View.VISIBLE
                        }




                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    }


                }
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val MATCHED_USER_ID = "MATCHED_USER_ID"
    }
}
