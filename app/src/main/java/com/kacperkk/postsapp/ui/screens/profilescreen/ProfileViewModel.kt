package com.kacperkk.postsapp.ui.screens.profilescreen

import androidx.compose.material3.Icon
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kacperkk.postsapp.model.UserProfile
import com.kacperkk.postsapp.model.posts.PostWithUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(

) : ViewModel() {

    sealed interface UIState {
        object Loading : UIState
        data class Error(val throwable: Throwable) : UIState
        data class Success(val data: List<PostWithUser>) : UIState
    }

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState



    init {
        getLocation()
    }

    fun getLocation() {

    }
}
