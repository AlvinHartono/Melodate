package com.example.melodate.ui.notifications

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melodate.BuildConfig
import com.example.melodate.data.Result
import com.example.melodate.data.model.Message
import com.example.melodate.data.socketio.SocketManager
import com.example.melodate.databinding.ActivityChatRoomBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding
    private val messageList = ArrayList<Message>()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var chatViewModel: ChatViewModel

    private var imageUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private lateinit var currentUserId: String

    // Permission request launchers
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openGallery()
        } else {
            Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Image result launchers
    private val cameraResultLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            val localUri = imageUri // Make a local copy
            localUri?.let { uri ->
                uploadImage(uri)
            }
        }
    }

    private val galleryResultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            uploadImage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cleanupTempFiles()
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get chatviewmodel from the factory
        chatViewModel =
            ViewModelProvider(this, ChatViewModelFactory.getInstance())[ChatViewModel::class.java]

        val userName = intent.getStringExtra("userName") ?: "User"
        val roomId = intent.getStringExtra("roomId") ?: ""
        val userId = intent.getIntExtra("userId", 1)
        currentUserId = intent.getIntExtra("currentUserId", 1).toString()

        val serverUrl = BuildConfig.CHAT_URL
        val dummyRoom = "secret_room"
        val dummyUserName = "U1"




        Log.d("ChatRoomActivity", "Room ID: $roomId")
        initializeSocket(serverUrl)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarTitle.text = userName
        setupRecyclerView()

        // Join the chat room
        Log.d("ChatRoomActivity", "Joining room: $roomId")
        joinChatRoom(roomId)

        binding.fabSend.setOnClickListener {
            val message = binding.editTextChat.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(roomId = roomId, senderId = currentUserId.toInt(), message = message)
                binding.editTextChat.text.clear()
            }
        }

        binding.fabImage.setOnClickListener {
            showImagePickDialog()
        }

        chatViewModel.sendImageState.observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }

                Result.Loading -> {}
                is Result.Success -> {
                    Toast.makeText(this, "Image sent successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Listen for incoming messages
        listenForMessages()
    }

    private fun showImagePickDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Choose Image Source")
            .setItems(arrayOf("Camera", "Gallery")) { _, which ->
                when (which) {
                    0 -> requestCameraPermission()
                    1 -> requestGalleryPermission()
                }
            }
            .show()
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                // Show an explanation to the user
                Toast.makeText(
                    this,
                    "Camera permission is needed to take photos",
                    Toast.LENGTH_SHORT
                ).show()
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun requestGalleryPermission() {
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
                Toast.makeText(
                    this,
                    "Storage permission is needed to select photos",
                    Toast.LENGTH_SHORT
                ).show()
                galleryPermissionLauncher.launch(permission)
            }

            else -> {
                galleryPermissionLauncher.launch(permission)
            }
        }
    }
    private fun openCamera() {
        val photoFile = createImageFile()
        val uri = FileProvider.getUriForFile(
            this,
            "com.example.melodate.provider",
            photoFile
        )
        imageUri = uri // Store the Uri in the mutable property
        cameraResultLauncher.launch(uri) // Use the local variable `uri`
    }

    private fun openGallery() {
        galleryResultLauncher.launch("image/*")
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
    private fun uploadImage(uri: Uri) {
        try {
            // Convert Uri to File
            val file = File(getFilePathFromUri(uri) ?: return)

            // Create RequestBody and MultipartBody.Part
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestFile
            )

            // Prepare other request bodies
            val roomIdBody = "secret_room".toRequestBody(null)
            val senderBody = "U1".toRequestBody(null)

            // Call ViewModel to send image
            lifecycleScope.launch {
                chatViewModel.sendImage(roomIdBody, senderBody, imagePart)
            }
        } catch (e: Exception) {
            Log.e("ChatRoomActivity", "Error uploading image", e)
            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        // Check if the Uri scheme is content
        if (uri.scheme == "content") {
            // For content:// URIs, especially from gallery
            try {
                // Modern method for Android 10 and above
                val inputStream = contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    // Create a temporary file
                    val tempFile = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
                    tempFile.createNewFile()
                    tempFile.outputStream().use { fileOut ->
                        inputStream.copyTo(fileOut)
                    }
                    inputStream.close()
                    return tempFile.absolutePath
                }
            } catch (e: Exception) {
                Log.e("ChatRoomActivity", "Error creating temp file", e)
            }
        }
        // Fallback for file:// URIs or other schemes
        else if (uri.scheme == "file") {
            return uri.path
        }

        return null
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

    override fun onDestroy() {
        super.onDestroy()
        // Disconnect the socket and remove listeners when the activity is destroyed
        SocketManager.off("receive_message")
        SocketManager.disconnect()
    }

    private fun initializeSocket(serverUrl: String) {
        Log.d("SocketManager", "Initializing socket...")
        if (!SocketManager.isInitialized()) {
            SocketManager.initialize(serverUrl)
            Log.d("SocketManager", "Socket initialized")
            SocketManager.connect()
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(this, messageList)
        binding.recyclerViewChat.adapter = messageAdapter
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
    }

    private fun joinChatRoom(roomId: String) {

        Log.d("ChatRoomActivity", "Joining room: $roomId")
        SocketManager.joinChatRoom("join_room", roomId)
        Log.d("ChatRoomActivity", "Joined room: $roomId")
        Toast.makeText(this, "Joined room: $roomId", Toast.LENGTH_SHORT).show()

        //listen for previous messages when joining the room
        SocketManager.on("load_previous_messages") { args ->
            if (args.isNotEmpty()) {
                val previousMessages = args[0]
                runOnUiThread {
                    // Clear existing messages
                    messageList.clear()

                    // Handle different possible message format types
                    when (previousMessages) {
                        is JSONArray -> {
                            // If it's a JSONArray, convert it to a list of messages
                            for (i in 0 until previousMessages.length()) {
                                val messageData = previousMessages.getJSONObject(i)
                                val sender = messageData.optString("sender", "Unknown")
                                val message = messageData.optString("message", "")
                                messageList.add(Message(message, sender.toInt(), currentUserId = currentUserId.toInt()))
                            }
                        }

                        is List<*> -> {
                            // If it's already a List, process it as before
                            (previousMessages as List<Map<String, Any>>).forEach { messageData ->
                                val sender = messageData["sender"] as? String ?: "Unknown"
                                val message = messageData["message"] as? String ?: ""
                                messageList.add(Message(message, sender.toInt(), currentUserId = currentUserId.toInt()))
                            }
                        }

                        else -> {
                            // Log an error if the format is unexpected
                            Log.e(
                                "ChatRoomActivity",
                                "Unexpected type for previous messages: ${previousMessages.javaClass}"
                            )
                        }
                    }

                    messageAdapter.notifyDataSetChanged()
                    if (messageList.isNotEmpty()) {
                        binding.recyclerViewChat.scrollToPosition(messageList.size - 1)
                    }
                }
            }
        }
    }

    private fun cleanupTempFiles() {
        val cacheDir = cacheDir
        val tempFiles = cacheDir.listFiles { file ->
            file.name.startsWith("temp_image_") && file.name.endsWith(".jpg")
        }

        tempFiles?.forEach { file ->
            // Delete files older than a day
            if (System.currentTimeMillis() - file.lastModified() > 24 * 60 * 60 * 1000) {
                file.delete()
            }
        }
    }

    private fun sendMessage(roomId: String, senderId: Int, message: String) {
        val messageData = JSONObject().apply {
            put("roomId", roomId)
            put("sender", senderId)
            put("message", message)
        }
        Log.d("ChatRoomActivity", "Sending message: $messageData")
        SocketManager.emit("send_message", messageData)

        // Add message to the local list and update UI
        binding.recyclerViewChat.scrollToPosition(messageList.size - 1)
    }

    private fun listenForMessages() {

        SocketManager.on("receive_message") { args ->
            if (args.isNotEmpty()) {
                val data = args[0] as JSONObject
                val sender = data.optString("sender", "Unknown")
                val message = data.optString("message", "")

                runOnUiThread {
                    messageList.add(Message(message, sender.toInt(), currentUserId = currentUserId.toInt()))
                    messageAdapter.notifyItemInserted(messageList.size - 1)
                    binding.recyclerViewChat.scrollToPosition(messageList.size - 1)
                }
            }
        }
    }
}
