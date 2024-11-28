package com.example.melodate.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.melodate.R
import com.example.melodate.databinding.ActivityRegisterUserPersonalDataBinding
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory

class RegisterUserPersonalDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserPersonalDataBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onResume() {
        super.onResume()
        setupDropDown()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserPersonalDataBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupGender()
        setupListeners()
        setupObservers()
    }

    private fun setupGender() {
        // Set both buttons to gray initially
        binding.maleButton.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_400))
        binding.femaleButton.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_400))

        binding.maleButton.setOnClickListener {
            authViewModel.setGender("Male")

            // Set female button to gray and male button to primaryColor
            binding.femaleButton.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_400))
            binding.maleButton.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.primaryColor
                )
            )
        }

        binding.femaleButton.setOnClickListener {
            authViewModel.setGender("Female")

            // Set male button to gray and female button to primaryColor
            binding.maleButton.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_400))
            binding.femaleButton.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.primaryColor
                )
            )
        }
    }

    private fun setupObservers() {
        authViewModel.userData.observe(this) { userData ->
            binding.etFirstName.setText(userData.name)
            binding.etDob.setText(userData.dob)
            binding.autoCompleteRelationshipStatus.setText(userData.status)
            binding.autoCompleteReligion.setText(userData.religion)
            binding.autoCompleteEducation.setText(userData.education)

            if (userData.gender != null) {
                if (userData.gender == "Male") {
                    binding.maleButton.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.primaryColor
                        )
                    )
                    binding.femaleButton.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.gray_400
                        )
                    )
                } else {
                    binding.maleButton.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.gray_400
                        )
                    )
                    binding.femaleButton.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.primaryColor
                        )
                    )
                }
            }
        }

        authViewModel.name.observe(this) { name ->
            if (binding.etFirstName.text.toString() != name) {
                binding.etFirstName.setText(name)
            }
        }

        authViewModel.gender.observe(this) { gender ->
            if (authViewModel.gender.value != "null") {
                if (gender == "Male") {
                    binding.maleButton.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.primaryColor
                        )
                    )
                    binding.femaleButton.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.gray_400
                        )
                    )
                } else {
                    binding.maleButton.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.gray_400
                        )
                    )
                    binding.femaleButton.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.primaryColor
                        )
                    )
                }
            }
        }

        authViewModel.dob.observe(this) { dob ->
            if (binding.etDob.text.toString() != dob) {
                binding.etDob.setText(dob)
            }
        }

        authViewModel.relationshipStatus.observe(this) { relationshipStatus ->
            if (binding.autoCompleteRelationshipStatus.text.toString() != relationshipStatus) {
                binding.autoCompleteRelationshipStatus.setText(relationshipStatus)
            }
        }

        authViewModel.education.observe(this) { education ->
            if (binding.autoCompleteEducation.text.toString() != education) {
                binding.autoCompleteEducation.setText(education)
            }
        }

        authViewModel.religion.observe(this) { religion ->
            if (binding.autoCompleteReligion.text.toString() != religion) {
                binding.autoCompleteReligion.setText(religion)
            }
        }

    }

    private fun setupDropDown() {
        val status = resources.getStringArray(R.array.status)
        val religion = resources.getStringArray(R.array.religion)
        val education = resources.getStringArray(R.array.education)

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, status)
        binding.autoCompleteRelationshipStatus.setAdapter(arrayAdapter)
        val arrayAdapter2 = ArrayAdapter(this, R.layout.dropdown_item, religion)
        binding.autoCompleteReligion.setAdapter(arrayAdapter2)
        val arrayAdapter3 = ArrayAdapter(this, R.layout.dropdown_item, education)
        binding.autoCompleteEducation.setAdapter(arrayAdapter3)
    }

    private fun setupListeners() {
        binding.etFirstName.addTextChangedListener { text ->
            authViewModel.setName(text.toString())
        }

        binding.etDob.addTextChangedListener { text ->
            authViewModel.setDob(text.toString())
        }
        binding.autoCompleteRelationshipStatus.addTextChangedListener { text ->
            authViewModel.setRelationshipStatus(text.toString())
        }
        binding.autoCompleteReligion.addTextChangedListener { text ->
            authViewModel.setReligion(text.toString())
        }

        binding.autoCompleteEducation.addTextChangedListener { text ->
            authViewModel.setEducation(text.toString())
        }


        binding.fabForward.setOnClickListener {
            val name = binding.etFirstName.text.toString()
            val dob = binding.etDob.text.toString()
            val relationshipStatus = binding.autoCompleteRelationshipStatus.text.toString()
            val religion = binding.autoCompleteReligion.text.toString()
            val education = binding.autoCompleteEducation.text.toString()
            val gender = authViewModel.gender.value.toString()

            authViewModel.updatePersonalData(
                name = name,
                dob = dob,
                status = relationshipStatus,
                religion = religion,
                education = education,
                gender = gender
            )
            val intent = Intent(
                this@RegisterUserPersonalDataActivity,
                RegisterUserGeneralInterestActivity::class.java
            )
            startActivity(intent)
        }

        binding.fabBack.setOnClickListener {
            finish()
        }

    }


}