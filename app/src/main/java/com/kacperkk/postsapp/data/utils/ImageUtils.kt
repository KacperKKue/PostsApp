package com.kacperkk.postsapp.data.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object ImageUtils {
    suspend fun saveProfileImage(context: Context, imageUri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val filename = "profile_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, filename)

                context.contentResolver.openInputStream(imageUri)?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun deleteOldImage(context: Context, oldPath: String?) {
        oldPath?.let {
            withContext(Dispatchers.IO) {
                File(oldPath).delete()
            }
        }
    }
}