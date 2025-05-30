package com.kacperkk.postsapp.data

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(
    private val context: Context
) {
    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val PROFILE_PIC_PATH = stringPreferencesKey("profile_pic_path")
    }

    suspend fun saveUserName(context: Context, name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }

    suspend fun saveProfileImage(uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            val filename = "profile_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, filename)

            context.contentResolver.openInputStream(uri)?.use { input ->
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

    suspend fun saveProfilePicPath(path: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PROFILE_PIC_PATH] = path
        }
    }

    val userName: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.USER_NAME] }

    val profilePicPath: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.PROFILE_PIC_PATH] }

    suspend fun deleteOldProfileImage(oldPath: String?) {
        oldPath?.let {
            withContext(Dispatchers.IO) {
                File(oldPath).delete()
            }
        }
    }
}