package com.kacperkk.postsapp.data.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

object ImageUtils {
    fun saveImageToInternalStorage(context: Context, uri: Uri): String {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputStream: OutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return fileName
    }

    fun getFileFromInternalStorage(context: Context, fileName: String): File {
        return File(context.filesDir, fileName)
    }
}