package com.example.melodate.ui.register

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.melodate.R
import com.example.melodate.databinding.ActivityRegisterUserPersonalDataBinding
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        setupIcons()
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
                }

                if (gender == "Female"){
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

        binding.etDob.setOnClickListener {
            showDatePicker()
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

        binding.fabBack.setOnClickListener {
            finish()
        }

        binding.fabForward.setOnClickListener {
            val name = binding.etFirstName.text.toString()
            val dob = binding.etDob.text.toString()
            val relationshipStatus = binding.autoCompleteRelationshipStatus.text.toString()
            val religion = binding.autoCompleteReligion.text.toString()
            val education = binding.autoCompleteEducation.text.toString()
            val gender = authViewModel.gender.value.toString()

            if (dob.isEmpty()) {
                binding.etDob.error = "Please select your date of birth"
                return@setOnClickListener
            }


            val age = calculateAge(dob)
            if (age < 18) {
                showErrorDialog("You must be at least 18 years old to register.")
            } else {
                showAgeConfirmationDialog(age) {

                    authViewModel.updatePersonalData(
                        name = name,
                        dob = dob,
                        status = relationshipStatus,
                        religion = religion,
                        education = education,
                        gender = gender,
                        age = age
                    )
                    val intent = Intent(
                        this@RegisterUserPersonalDataActivity,
                        RegisterUserGeneralInterestActivity::class.java
                    )
                    val options = ActivityOptionsCompat.makeCustomAnimation(
                        this@RegisterUserPersonalDataActivity,
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )
                    startActivity(intent, options.toBundle())
                }
            }
        }
    }

    private fun showDatePicker() {
        // Get the current date to set as default in the picker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create and show the DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the EditText with the selected date

                val formattedDate =
                    String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.etDob.setText(formattedDate)
                authViewModel.setDob(formattedDate) // Update ViewModel
            },
            year, month, day
        )

        // Optionally set a maximum date (e.g., today for DOB)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    private fun setupIcons() {
        // Check the current theme (light or dark)
        val isDarkTheme = (resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES

        // Select the appropriate drawable based on the theme
        val calendarIcon = if (isDarkTheme) {
            AppCompatResources.getDrawable(this, R.drawable.ic_calendar_dark)
        } else {
            AppCompatResources.getDrawable(this, R.drawable.ic_calendar_light)
        }

        // Set the drawable to the EditText (start icon)
        binding.etDob.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,  // No drawable at the start
            null,  // No drawable at the top
            calendarIcon,  // Calendar icon at the end
            null   // No drawable at the bottom
        )
    }

    private fun calculateAge(dobText: String): Int {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dob = formatter.parse(dobText)
        val dobCalendar = Calendar.getInstance().apply {
            if (dob != null) {
                time = dob
            }
        }
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }

    private fun showAgeConfirmationDialog(age: Int, onConfirm: () -> Unit) {
        val message = "Your age is $age years old. Do you want to continue with this information?"

        AlertDialog.Builder(this)
            .setTitle("Confirm Age")
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ ->
                onConfirm() // Continue registration
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Close dialog
            }
            .show()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


}