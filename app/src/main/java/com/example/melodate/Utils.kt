package com.example.melodate

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private const val MAXIMAL_SIZE = 1_000_000
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())


fun uriToFile(uri: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver

    val tempFile = File.createTempFile(
        "temp_",
        null,
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    )

    val inputStream: InputStream? = contentResolver.openInputStream(uri)

    val outputStream = FileOutputStream(tempFile)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    return tempFile
}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    val bmpStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)

    if (bmpStream.size() <= MAXIMAL_SIZE) return file

    do {
        bmpStream.reset()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        compressQuality -= 5
    } while (bmpStream.size() > MAXIMAL_SIZE)

    FileOutputStream(file).use {
        it.write(bmpStream.toByteArray())
    }
    return file
}

fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}