package com.example.melodate.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
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
        val yes = ContextCompat.getString(
            this,
            R.string.yes
        )
        val no = ContextCompat.getString(
            this,
            R.string.no
        )

        authViewModel.userData.observe(this) { userData ->
            binding.editTextHobby.setText(userData.hobby)

            // Handle isSmoker
            when (userData.isSmoker) {
                true -> binding.autoCompleteSmoking.setText(yes)
                false -> binding.autoCompleteSmoking.setText(no)
                null -> binding.autoCompleteSmoking.setText("") // or set a default value
            }

            // Handle isDrinker
            when (userData.isDrinker) {
                true -> binding.autoCompleteDrinking.setText(yes)
                false -> binding.autoCompleteDrinking.setText(no)
                null -> binding.autoCompleteDrinking.setText("") // or set a default value
            }
            if (userData.height.toString() != "null") {
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
            val isSmoker = text.toString() == "Yes"

            val smokerFromAuth = authViewModel.isSmoker.value == "Yes"

            if (smokerFromAuth != isSmoker) {
                authViewModel.setIsSmoker(isSmoker)
            }
        }
        binding.autoCompleteDrinking.addTextChangedListener { text ->
            val isDrinker = text.toString() == "Yes"
            val drinkerFromAuth = authViewModel.isDrinker.value == "Yes"
            if (drinkerFromAuth != isDrinker) {
                authViewModel.setIsDrinker(isDrinker)
            }
        }
        binding.autoCompleteMbti.addTextChangedListener { text ->
            authViewModel.setMbti(text.toString())
        }
        binding.autoCompleteLoveLanguage.addTextChangedListener { text ->
            authViewModel.setLoveLang(text.toString())
        }

        binding.fabForward.setOnClickListener {
            val hobby = authViewModel.hobby.value ?: ""
            val height = authViewModel.height.value?.toInt() ?: 0
            val isSmoker = authViewModel.isSmoker.value ?: ""
            val isDrinker = authViewModel.isDrinker.value ?: ""
            val mbti = authViewModel.mbti.value ?: ""
            val loveLang = authViewModel.loveLang.value ?: ""

            authViewModel.updateGeneralInterestData(
                hobby = hobby,
                height = height,
                isSmoker = isSmoker,
                isDrinker = isDrinker,
                mbti = mbti,
                loveLang = loveLang,
            )

            val intent = Intent(
                this@RegisterUserGeneralInterestActivity,
                RegisterMusicPreferenceActivity::class.java
            )
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this@RegisterUserGeneralInterestActivity,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            startActivity(intent, options.toBundle())
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