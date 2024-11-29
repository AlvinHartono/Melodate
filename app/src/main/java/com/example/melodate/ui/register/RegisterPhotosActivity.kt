package com.example.melodate.ui.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.melodate.R
import com.example.melodate.databinding.ActivityRegisterPhotosBinding
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory

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
        authViewModel.selectedImages.observe(this) { uris ->

//            uris.forEachIndexed { index, uri ->
//                if (uri != Uri.EMPTY) {
//                    loadImageIntoView(index, uri)
//                    imageStates[index] = true
//                } else {
//                    resetImageView(index)
//                    imageStates[index] = false
//                }
//            }

        }
    }

    private fun setupListeners() {
        binding.image1.setOnClickListener {
            showImageOptionsDialog(0)
        }
        binding.image2.setOnClickListener {
            showImageOptionsDialog(1)
        }
        binding.image3.setOnClickListener {
            showImageOptionsDialog(2)
        }
        binding.image4.setOnClickListener {
            showImageOptionsDialog(3)
        }
        binding.image5.setOnClickListener {
            showImageOptionsDialog(4)
        }
        binding.image6.setOnClickListener {
            showImageOptionsDialog(5)
        }

        binding.fabBack.setOnClickListener {
            finish()
        }
        binding.buttonRegister.setOnClickListener {

            val intent = Intent(this@RegisterPhotosActivity, RegisterFinishedActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
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
        when (imagePosition) {
            0 -> {
                binding.image1.setImageResource(defaultDrawableRes)
                imageStates[0] = false
            }

            1 -> {
                binding.image2.setImageResource(defaultDrawableRes)
                imageStates[1] = false
            }

            2 -> {
                binding.image3.setImageResource(defaultDrawableRes)
                imageStates[2] = false
            }

            3 -> {
                binding.image4.setImageResource(defaultDrawableRes)
                imageStates[3] = false
            }

            4 -> {
                binding.image5.setImageResource(defaultDrawableRes)
                imageStates[4] = false
            }

            5 -> {
                binding.image6.setImageResource(defaultDrawableRes)
                imageStates[5] = false
            }
        }
        authViewModel.addOrReplaceImage(
            imagePosition,
            Uri.EMPTY
        )
    }

}