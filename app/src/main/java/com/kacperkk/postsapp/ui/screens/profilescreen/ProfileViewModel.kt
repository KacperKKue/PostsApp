package com.kacperkk.postsapp.ui.screens.profilescreen

import android.adservices.ondevicepersonalization.UserData
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Icon
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kacperkk.postsapp.PostsApp
import com.kacperkk.postsapp.data.UserPreferencesRepository
import com.kacperkk.postsapp.model.posts.PostWithUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val userNameFlow: Flow<String> = userPreferencesRepository.userName
    val userSurnameFlow: Flow<String> = userPreferencesRepository.userSurname
    val avatarPathFlow: Flow<String> = userPreferencesRepository.avatarPath

    fun updateUserName(name: String) {
        viewModelScope.launch {
            userPreferencesRepository.setUserName(name)
        }
    }

    fun updateUserSurname(surname: String) {
        viewModelScope.launch {
            userPreferencesRepository.setUserSurname(surname)
        }
    }

    fun updateAvatarPath(path: String) {
        viewModelScope.launch {
            userPreferencesRepository.setAvatarPath(path)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PostsApp
                val repo = app.container.userPreferencesRepository
                ProfileViewModel(repo)
            }
        }
    }
}
