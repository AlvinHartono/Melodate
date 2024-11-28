package com.example.melodate.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.melodate.R
import com.example.melodate.databinding.ActivityRegisterUserGeneralInterestBinding
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory

class RegisterUserGeneralInterestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserGeneralInterestBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onResume() {
        super.onResume()
        setupDropDowns()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterUserGeneralInterestBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
        setupObservers()


    }

    private fun setupObservers() {
        authViewModel.userData.observe(this) { userData ->
            binding.editTextHobby.setText(userData.hobby)

            if (userData.isSmoker.toString() != "null"){
                if (userData.isSmoker == true){
                    //use resource string.xml
                    binding.autoCompleteSmoking.setText("Yes")
                }else{
                    binding.autoCompleteSmoking.setText("No")
                }

            }
            if (userData.isDrinker.toString() != "null"){
                // if true, Yes. if false, No
                if (userData.isDrinker == true){
                    binding.autoCompleteDrinking.setText("Yes")
                }else{
                    binding.autoCompleteDrinking.setText("No")
                }
            }
            if (userData.height.toString() != "null"){

                binding.editTextHeight.setText(userData.height.toString())
            }

            binding.autoCompleteMbti.setText(userData.mbti)
            binding.autoCompleteLoveLanguage.setText(userData.loveLang)
        }

        authViewModel.hobby.observe(this) { hobby ->
            if (binding.editTextHobby.text.toString() != hobby) {
                binding.editTextHobby.setText(hobby)
            }
        }
        authViewModel.height.observe(this) { height ->
            if (binding.editTextHeight.text.toString() != height) {
                binding.editTextHeight.setText(height)
            }
        }
        authViewModel.isSmoker.observe(this) { isSmoker ->
            if (binding.autoCompleteSmoking.text.toString() != isSmoker) {
                binding.autoCompleteSmoking.setText(isSmoker)
            }
        }

        authViewModel.isDrinker.observe(this) { isDrinker ->
            if (binding.autoCompleteDrinking.text.toString() != isDrinker) {
                binding.autoCompleteDrinking.setText(isDrinker)
            }
        }
        authViewModel.mbti.observe(this) { mbti ->
            if (binding.autoCompleteMbti.text.toString() != mbti) {
                binding.autoCompleteMbti.setText(mbti)
            }
        }
        authViewModel.loveLang.observe(this) { loveLang ->
            if (binding.autoCompleteLoveLanguage.text.toString() != loveLang) {
                binding.autoCompleteLoveLanguage.setText(loveLang)
            }
        }

    }

    private fun setupListeners() {
        binding.editTextHobby.addTextChangedListener { text ->
            authViewModel.setHobby(text.toString())
        }

        binding.editTextHeight.addTextChangedListener { text ->
            authViewModel.setHeight(text.toString())
        }
        binding.autoCompleteSmoking.addTextChangedListener { text ->
            authViewModel.setIsSmoker(text.toString())
        }
        binding.autoCompleteDrinking.addTextChangedListener { text ->
            authViewModel.setIsDrinker(text.toString())
        }
        binding.autoCompleteMbti.addTextChangedListener { text ->
            authViewModel.setMbti(text.toString())
        }
        binding.autoCompleteLoveLanguage.addTextChangedListener { text ->
            authViewModel.setLoveLang(text.toString())
        }

        binding.fabForward.setOnClickListener {
            val hobby = binding.editTextHobby.text.toString()
            val height = binding.editTextHeight.text.toString()
            val isSmoker = binding.autoCompleteSmoking.text.toString()
            val isDrinker = binding.autoCompleteDrinking.text.toString()
            val mbti = binding.autoCompleteMbti.text.toString()
            val loveLang = binding.autoCompleteLoveLanguage.text.toString()

            authViewModel.updateGeneralInterestData(
                hobby = hobby,
                height = height.toInt(),
                isSmoker = isSmoker,
                isDrinker = isDrinker,
                mbti = mbti,
                loveLang = loveLang,
            )

            val intent = Intent(
                this@RegisterUserGeneralInterestActivity,
                RegisterMusicPreferenceActivity::class.java
            )
            startActivity(intent)
        }
        binding.fabBack.setOnClickListener {
            finish()
        }
    }

    private fun setupDropDowns() {
        val smoking = resources.getStringArray(R.array.smoking)
        val drinking = resources.getStringArray(R.array.drinking)
        val mbti = resources.getStringArray(R.array.MBTI)
        val loveLanguages = resources.getStringArray(R.array.love_languages)

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, smoking)
        binding.autoCompleteSmoking.setAdapter(arrayAdapter)
        val arrayAdapter2 = ArrayAdapter(this, R.layout.dropdown_item, drinking)
        binding.autoCompleteDrinking.setAdapter(arrayAdapter2)
        val arrayAdapter3 = ArrayAdapter(this, R.layout.dropdown_item, mbti)
        binding.autoCompleteMbti.setAdapter(arrayAdapter3)
        val arrayAdapter4 = ArrayAdapter(this, R.layout.dropdown_item, loveLanguages)
        binding.autoCompleteLoveLanguage.setAdapter(arrayAdapter4)
    }
}