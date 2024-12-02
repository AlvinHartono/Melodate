package com.example.melodate.ui.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.melodate.R
import com.example.melodate.data.Result
import com.example.melodate.databinding.ActivityRegisterPhotosBinding
import com.example.melodate.reduceFileImage
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory
import com.example.melodate.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RegisterPhotosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPhotosBinding
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var currentImagePosition: Int = -1
    private val imageStates = BooleanArray(6) { false }
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

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

        // Initialize the launcher
        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    handleImageSelection(uri)
                }
            }
        }

        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        authViewModel.registerState.observe(this) { response ->
            when (response) {
                is Result.Error -> {
                    val errorMessage = response.error
                    androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(errorMessage)
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()

                    Log.d("RegisterPhotosActivity", "Error: $errorMessage")
                }

                Result.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                    Log.d("RegisterPhotosActivity", "Loading")
                }

                is Result.Success -> {
                    val intent = Intent(this, RegisterFinishedActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
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

        binding.fabBack.setOnClickListener {
            finish()
        }
        binding.buttonRegister.setOnClickListener {
            val user = authViewModel.userData.value

            val selectedImages = authViewModel.selectedImages.value ?: emptyList()

            // Ensure at least 4 images are provided
            val imageFiles = selectedImages.take(6).map { uri ->
                uriToFile(uri, this).reduceFileImage()
            }

            if (imageFiles.size < 4) {
                // Notify the user that at least 4 images are required
                Toast.makeText(
                    this,
                    "Please upload at least 4 photos to continue.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

//             //Pad the list to ensure 6 file slots for backend processing
            val paddedImageFiles = imageFiles + List(6 - imageFiles.size) { null }

            val parts = paddedImageFiles.mapIndexed { index, file ->
                file?.let {
                    createFilePart("profilePicture${index + 1}", it)
                }
            }

            lifecycleScope.launch {
                authViewModel.registerUser(
                    email = createRequestBody(user?.email ?: ""),
                    password = createRequestBody(user?.password ?: ""),
                    confirmPassword = createRequestBody(user?.password ?: ""),
                    firstName = createRequestBody(user?.name ?: ""),
                    age = createRequestBody(user?.age.toString()),
                    dateOfBirth = createRequestBody(user?.dob ?: ""),
                    gender = createRequestBody(user?.gender ?: ""),
                    relationshipStatus = createRequestBody(user?.status ?: ""),
                    education = createRequestBody(user?.education ?: ""),
                    religion = createRequestBody(user?.religion ?: ""),
                    hobby = createRequestBody(user?.hobby ?: ""),
                    height = createRequestBody(user?.height.toString()),
                    smoking = createRequestBody(user?.isSmoker.toString()),
                    drinking = createRequestBody(user?.isDrinker.toString()),
                    mbti = createRequestBody(user?.mbti ?: ""),
                    loveLanguage = createRequestBody(user?.loveLang ?: ""),
                    genre = createRequestBody(user?.genre ?: ""),
                    musicDecade = createRequestBody(user?.musicDecade ?: ""),
                    musicVibe = createRequestBody(user?.musicVibe ?: ""),
                    listeningFrequency = createRequestBody(user?.listeningFrequency ?: ""),
                    concert = createRequestBody(user?.concert ?: ""),
                    profilePicture1 = parts.getOrNull(0),
                    profilePicture2 = parts.getOrNull(1),
                    profilePicture3 = parts.getOrNull(2),
                    profilePicture4 = parts.getOrNull(3),
                    profilePicture5 = parts.getOrNull(4),
                    profilePicture6 = parts.getOrNull(5)
                )
            }

//            val intent = Intent(this@RegisterPhotosActivity, RegisterFinishedActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            finish()
        }
    }

    private fun handleImageSelection(uri: Uri) {
        when (currentImagePosition) {
            0 -> {
                Glide.with(this).load(uri).into(binding.image1)
                imageStates[0] = true
            }

            1 -> {
                Glide.with(this).load(uri).into(binding.image2)
                imageStates[1] = true
            }

            2 -> {
                Glide.with(this).load(uri).into(binding.image3)
                imageStates[2] = true
            }

            3 -> {
                Glide.with(this).load(uri).into(binding.image4)
                imageStates[3] = true
            }

            4 -> {
                Glide.with(this).load(uri).into(binding.image5)
                imageStates[4] = true
            }

            5 -> {
                Glide.with(this).load(uri).into(binding.image6)
                imageStates[5] = true
            }
        }
        authViewModel.addOrReplaceImage(currentImagePosition, uri)
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
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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

    private fun createRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun createFilePart(fieldName: String, file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fieldName, file.name, requestFile)
    }

}