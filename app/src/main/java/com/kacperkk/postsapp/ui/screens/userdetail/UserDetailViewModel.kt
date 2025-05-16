package com.kacperkk.postsapp.ui.screens.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.fetch.Fetcher
import com.kacperkk.postsapp.PostsApp
import com.kacperkk.postsapp.data.PostsRepository
import com.kacperkk.postsapp.model.Todo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kacperkk.postsapp.ui.screens.postlist.PostListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.map

class UserDetailViewModel(
    private val repository: PostsRepository,
    private val userId: Int
) : ViewModel() {

    sealed interface UIState {
        object Loading : UIState
        data class Error(val throwable: Throwable) : UIState
        data class Success(val todos: List<Todo>) : UIState
    }

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    init {
        fetchData()
    }

    internal fun fetchData() {
        viewModelScope.launch {
            try {
                val todos = repository.getTodo(userId)
                _uiState.value = UIState.Success(todos)
            } catch (e: Exception) {
                _uiState.value = UIState.Error(e)
            }
        }
    }

    companion object {
        fun provideFactory(userId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PostsApp
                val repository = app.container.postsRepository
                UserDetailViewModel(repository, userId)
            }
        }
    }
}