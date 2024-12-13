package com.example.melodate.ui.home.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.melodate.HomeActivity
import com.example.melodate.R
import com.example.melodate.databinding.ActivityEditProfileBinding
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model.UserViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory
import com.example.melodate.ui.shared.view_model_factory.UserViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.Locale
import kotlin.math.log

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentImagePosition: Int = -1
    private var cameraImageUri: Uri? = null
    private val imageStates = BooleanArray(6) { false }
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }
    private val userViewModel: UserViewModel by viewModels{
        UserViewModelFactory.getInstance(this)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onResume() {
        super.onResume()
        setupDropDowns()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Log.d("ProfileActivity", "ActivityResult callback triggered with resultCode: ${result.resultCode}")
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data ?: cameraImageUri
                Log.d("ProfileActivity", "Image URI from picker: $uri")
                if (uri != null) {
                    handleImageSelection(uri)
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //add back button
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Setup image view listeners
        setupImageViewListeners()
        setupListeners()
        setupObservers()
    }


    private fun handleImageSelection(uri: Uri) {
        Log.d("ProfileActivity", "Handling image selection for position $currentImagePosition with URI $uri")
        if (currentImagePosition in 0..5) {
            val imageViews = arrayOf(
                binding.image1, binding.image2, binding.image3,
                binding.image4, binding.image5, binding.image6
            )
            Glide.with(this).load(uri).into(imageViews[currentImagePosition])
            imageStates[currentImagePosition] = true
            authViewModel.addOrReplaceImage(currentImagePosition, uri)
        } else {
            Log.e("ProfileActivity", "Invalid image position: $currentImagePosition")
        }
    }


    private fun setupImageViewListeners() {
        val imageViews = arrayOf(
            binding.image1, binding.image2, binding.image3,
            binding.image4, binding.image5, binding.image6
        )

        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                showImageOptionsDialog(index)
            }
        }
    }

    private fun setupObservers() {
        authViewModel.userData.observe(this) { user ->
            if (user != null) {
                binding.editTextBio.setText(user.bio)
                binding.editTextHeight.setText(user.height.toString())
                binding.editTextHobby.setText(user.hobby.toString())
                binding.editTextLocation.setText(user.location)
                binding.autoCompleteRelationshipStatus.setText(user.status)
                binding.autoCompleteEducation.setText(user.education)
                binding.autoCompleteReligion.setText(user.religion)
                binding.autoCompleteSmoking.setText(if (user.isSmoker!!) "Yes" else "No")
                binding.autoCompleteDrinking.setText(if (user.isDrinker!!) "Yes" else "No")
                binding.autoCompleteMbti.setText(user.mbti)
                binding.autoCompleteLoveLanguage.setText(user.loveLang)
                binding.autoCompleteGenre.setText(user.genre)
                binding.autoCompleteMusicDecade.setText(user.musicDecade)
                binding.autoCompleteMusicVibe.setText(user.musicVibe)
                binding.autoCompleteListeningFrequency.setText(user.listeningFrequency)
                binding.autoCompleteConcert.setText(user.concert)

                Glide.with(this).load(user.picture1).into(binding.image1)
                Glide.with(this).load(user.picture2).into(binding.image2)
                Glide.with(this).load(user.picture3).into(binding.image3)
                Glide.with(this).load(user.picture4).into(binding.image4)
                if (user.picture5 != null) {
                    Glide.with(this).load(user.picture5).into(binding.image5)
                }
                if (user.picture6 != null) {
                    Glide.with(this).load(user.picture6).into(binding.image6)
                }

                val initialImages = arrayOf(
                    user.picture1?.let { Uri.parse(it) } ?: Uri.EMPTY,
                    user.picture2?.let { Uri.parse(it) } ?: Uri.EMPTY,
                    user.picture3?.let { Uri.parse(it) } ?: Uri.EMPTY,
                    user.picture4?.let { Uri.parse(it) } ?: Uri.EMPTY,
                    user.picture5?.let { Uri.parse(it) } ?: Uri.EMPTY,
                    user.picture6?.let { Uri.parse(it) } ?: Uri.EMPTY
                )
                authViewModel.updateSelectedImages(initialImages)
            }
        }
    }


    private fun setupListeners() {
        val imageViews = arrayOf(
            binding.image1, binding.image2, binding.image3,
            binding.image4, binding.image5, binding.image6
        )

        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                showImageOptionsDialog(index)
            }
        }

        binding.editTextLocation.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun showImageOptionsDialog(imagePosition: Int) {
        val isImageSet = imageStates[imagePosition]
        val options = if (isImageSet) {
            arrayOf("Replace Photo", "Remove Photo")
        } else {
            arrayOf("Choose from Gallery", "Take a Photo")
        }

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Select an Option")
        builder.setItems(options) { _, which ->
            when (options[which]) {
                "Choose from Gallery" -> openGallery(imagePosition)
                "Take a Photo" -> openCamera(imagePosition)
                "Replace Photo" -> openGallery(imagePosition)
                "Remove Photo" -> removeImage(imagePosition)
            }
        }
        builder.show()
    }

    private fun openGallery(imagePosition: Int) {
        currentImagePosition = imagePosition
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun openCamera(imagePosition: Int) {
        currentImagePosition = imagePosition

        // Create a file to save the image
        val imageFile = File.createTempFile("IMG_", ".jpg", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        // Get the URI for the file
        cameraImageUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            imageFile
        )

        // Launch the camera intent with the file URI
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
        }
        imagePickerLauncher.launch(intent)
    }

    private fun removeImage(imagePosition: Int) {
        val defaultDrawableRes = R.drawable.ic_add
        val imageViews = arrayOf(
            binding.image1, binding.image2, binding.image3,
            binding.image4, binding.image5, binding.image6
        )
        Glide.with(this).load(defaultDrawableRes).into(imageViews[imagePosition])
        imageStates[imagePosition] = false
        authViewModel.addOrReplaceImage(imagePosition, Uri.EMPTY)
    }



    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions if not already granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1001 // Request code for location permissions
            )
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    ?.toList() // Convert MutableList to List

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val province = address.adminArea ?: "Unknown Province"
                    val country = address.countryName ?: "Unknown Country"
                    binding.editTextLocation.setText("$province, $country")
                }
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to detect location: ${it.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.ic_check -> {
                saveProfile()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupDropDowns() {
        val status = resources.getStringArray(R.array.status)
        val religion = resources.getStringArray(R.array.religion)
        val education = resources.getStringArray(R.array.education)
        val smoking = resources.getStringArray(R.array.smoking)
        val drinking = resources.getStringArray(R.array.drinking)
        val mbti = resources.getStringArray(R.array.MBTI)
        val loveLanguages = resources.getStringArray(R.array.love_languages)
        val genres = resources.getStringArray(R.array.music_genres)
        val musicDecade = resources.getStringArray(R.array.music_decades)
        val musicVibe = resources.getStringArray(R.array.music_vibes)
        val listeningFrequency = resources.getStringArray(R.array.listening_frequency)
        val concert = resources.getStringArray(R.array.concert_preferences)


        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, status)
        binding.autoCompleteRelationshipStatus.setAdapter(arrayAdapter)
        val arrayAdapter2 = ArrayAdapter(this, R.layout.dropdown_item, religion)
        binding.autoCompleteReligion.setAdapter(arrayAdapter2)
        val arrayAdapter3 = ArrayAdapter(this, R.layout.dropdown_item, education)
        binding.autoCompleteEducation.setAdapter(arrayAdapter3)
        val arrayAdapter4 = ArrayAdapter(this, R.layout.dropdown_item, smoking)
        binding.autoCompleteSmoking.setAdapter(arrayAdapter4)
        val arrayAdapter5 = ArrayAdapter(this, R.layout.dropdown_item, drinking)
        binding.autoCompleteDrinking.setAdapter(arrayAdapter5)

        val arrayAdapter6 = ArrayAdapter(this, R.layout.dropdown_item, mbti)
        binding.autoCompleteMbti.setAdapter(arrayAdapter6)
        val arrayAdapter7 = ArrayAdapter(this, R.layout.dropdown_item, loveLanguages)
        binding.autoCompleteLoveLanguage.setAdapter(arrayAdapter7)
        val arrayAdapter8 = ArrayAdapter(this, R.layout.dropdown_item, genres)
        binding.autoCompleteGenre.setAdapter(arrayAdapter8)
        val arrayAdapter9 = ArrayAdapter(this, R.layout.dropdown_item, musicDecade)
        binding.autoCompleteMusicDecade.setAdapter(arrayAdapter9)
        val arrayAdapter10 = ArrayAdapter(this, R.layout.dropdown_item, musicVibe)
        binding.autoCompleteMusicVibe.setAdapter(arrayAdapter10)
        val arrayAdapter11 = ArrayAdapter(this, R.layout.dropdown_item, listeningFrequency)
        binding.autoCompleteListeningFrequency.setAdapter(arrayAdapter11)
        val arrayAdapter12= ArrayAdapter(this, R.layout.dropdown_item, concert)
        binding.autoCompleteConcert.setAdapter(arrayAdapter12)


    }

    private fun saveProfile() {
        val selectedImagesCount = authViewModel.selectedImages.value?.count { it != Uri.EMPTY } ?: 0
        Log.d("ProfileActivity", "Selected img count : $selectedImagesCount")

        if (selectedImagesCount < 4) {
            Toast.makeText(this, "Please upload at least 4 images.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImagesCount > 6) {
            Toast.makeText(this, "You can only upload a maximum of 6 images.", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Saving profile...", Toast.LENGTH_SHORT).show()
        Log.d("ProfileActivity", "Saving profile...")

        try {
            val status = createRequestBody(binding.autoCompleteRelationshipStatus.text.toString())
            val education = createRequestBody(binding.autoCompleteEducation.text.toString())
            val religion = createRequestBody(binding.autoCompleteReligion.text.toString())
            val height = createRequestBody(binding.editTextHeight.text.toString())
            val smoking = createRequestBody(binding.autoCompleteSmoking.text.toString())
            val drinking = createRequestBody(binding.autoCompleteDrinking.text.toString())
            val mbti = createRequestBody(binding.autoCompleteMbti.text.toString())
            val loveLanguage = createRequestBody(binding.autoCompleteLoveLanguage.text.toString())
            val genre = createRequestBody(binding.autoCompleteGenre.text.toString())
            val musicDecade = createRequestBody(binding.autoCompleteMusicDecade.text.toString())
            val musicVibe = createRequestBody(binding.autoCompleteMusicVibe.text.toString())
            val listeningFrequency =
                createRequestBody(binding.autoCompleteListeningFrequency.text.toString())
            val concert = createRequestBody(binding.autoCompleteConcert.text.toString())
            val location = createRequestBody(binding.editTextLocation.text.toString())
            val hobby = createRequestBody(binding.editTextHobby.text.toString())
            val bio = createRequestBody(binding.editTextBio.text.toString())

            val imageParts = mutableListOf<MultipartBody.Part>()
            authViewModel.selectedImages.value?.forEachIndexed { index, uri ->
                if (uri != Uri.EMPTY) {
                    val imageFile = File(uri.path!!)
                    val part = createFilePart("image$index", imageFile)
                    imageParts.add(part)
                }
            }

            userViewModel.editProfile(
                status = status,
                education = education,
                religion = religion,
                height = height,
                smoking = smoking,
                drinking = drinking,
                mbti = mbti,
                loveLanguage = loveLanguage,
                genre = genre,
                musicDecade = musicDecade,
                musicVibe = musicVibe,
                listeningFrequency = listeningFrequency,
                concert = concert,
                location = location,
                hobby = hobby,
                bio = bio
            )

            // Log success message
            Log.d("ProfileActivity", "Profile saved successfully!")

            // Show a success Toast
            Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show()

            // Start HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()  // Optionally finish the current activity

        } catch (e: Exception) {
            // Handle errors
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
                .setMessage(e.message)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .setNegativeButton("Close") { dialog, _ -> dialog.dismiss() }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }


    private fun createRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun createFilePart(fieldName: String, file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fieldName, file.name, requestFile)
    }
}
