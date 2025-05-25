package com.kacperkk.postsapp.ui.screens.profilescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kacperkk.postsapp.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile = _userProfile.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.userProfileFlow.collect {
                _userProfile.value = it
            }
        }
    }

    fun updateProfile(firstName: String, lastName: String, imagePath: String) {
        viewModelScope.launch {
            userPreferences.saveUserProfile(
                UserProfile(firstName, lastName, imagePath)
            )
        }
    }
}
