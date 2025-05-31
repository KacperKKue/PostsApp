package com.kacperkk.postsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val AVATAR_PATH_KEY = stringPreferencesKey("avatar_path")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_SURNAME_KEY = stringPreferencesKey("user_surname")
    }

    suspend fun setAvatarPath(path: String) {
        context.dataStore.edit { preferences ->
            preferences[AVATAR_PATH_KEY] = path
        }
    }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    suspend fun setUserSurname(surname: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_SURNAME_KEY] = surname
        }
    }

    val avatarPath: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[AVATAR_PATH_KEY] ?: "" }

    val userName: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME_KEY] ?: "" }

    val userSurname: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[USER_SURNAME_KEY] ?: "" }
}

